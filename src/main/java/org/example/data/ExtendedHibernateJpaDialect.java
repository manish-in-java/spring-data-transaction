package org.example.data;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Custom {@link HibernateJpaDialect} that allows setting custom
 * transaction isolation levels on database connections.  By
 * default, custom transaction isolation levels are not supported
 * by JPA and any attempt to customize the transaction isolation
 * level is rejecting by Spring Data JPA.
 */
public class ExtendedHibernateJpaDialect extends HibernateJpaDialect
{
  /**
   * @see org.springframework.orm.jpa.vendor.HibernateJpaDialect#beginTransaction(javax.persistence.EntityManager, org.springframework.transaction.TransactionDefinition)
   */
  @SuppressWarnings("deprecation")
  @Override
  public Object beginTransaction(final EntityManager entityManager
      , final TransactionDefinition definition)
      throws PersistenceException, SQLException, TransactionException
  {
    // Obtain the current Hibernate session.
    final Session session = (Session) entityManager.getDelegate();
    if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT)
    {
      // Set custom transaction timeout, if specified.
      session.getTransaction().setTimeout(definition.getTimeout());
    }

    // Capture current transaction data.
    final TransactionData transactionData = new TransactionData(prepareTransaction(entityManager, definition.isReadOnly(), definition.getName()));
    session.doWork(
        new Work()
        {
          @Override
          public void execute(final Connection connection) throws SQLException
          {
            transactionData.connection = connection;
          }
        }
    );

    transactionData.isolationLevel = DataSourceUtils.prepareConnectionForTransaction(transactionData.connection, definition);

    entityManager.getTransaction().begin();

    return transactionData;
  }

  /*
   * @see org.springframework.orm.jpa.vendor.HibernateJpaDialect#cleanupTransaction(java.lang.Object)
   */
  @Override
  public void cleanupTransaction(Object transactionData)
  {
    final TransactionData sessionTransactionData = (TransactionData) transactionData;

    super.cleanupTransaction(sessionTransactionData.sessionTransactionData);

    sessionTransactionData.resetIsolationLevel();
  }

  /**
   * Encapsulates important data about a transaction so that
   * any changes to the transaction isolation can be reset
   * after the current query has finished executing on the
   * transaction.
   */
  private static class TransactionData
  {
    Connection connection;
    Integer    isolationLevel;
    final Object sessionTransactionData;

    /**
     * Sets the data originally associated with the transaction.
     *
     * @param sessionTransactionData An {@link Object}.
     */
    TransactionData(final Object sessionTransactionData)
    {
      this.sessionTransactionData = sessionTransactionData;
    }

    /**
     * Resets the isolation level on the transaction.
     */
    void resetIsolationLevel()
    {
      if (isolationLevel != null)
      {
        DataSourceUtils.resetConnectionAfterTransaction(connection, isolationLevel);
      }
    }
  }
}
