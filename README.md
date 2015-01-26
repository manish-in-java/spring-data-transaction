# Background
----

Many business applications have a requirement to generate sequential identifiers for business documents, such as,
invoices, goods receipts, goods dispatches, and so on.  Many countries legally require such document identifiers
to be not only sequential but without any gaps too.  Some countries impose additional legal requirements on such
identifiers, like prefixing the identifiers with the financial year in which they were generated, resetting them
every financial year, and so on.

Developers looking to implement such a requirement are typically faced with the following challenges:

1. **Uniqueness**: Each generated identifier must be unique.
1. **Sequential**: The generated identifiers must be sequential in nature within a given time period, that is,
the identifiers must follow a strict sequence such as `10001`, `10002` and so on.
1. **No gaps**: There must not be any gaps in the generated identifiers, meaning valid identifiers must not be
skipped.
1. **Concurrency**: Multiple application users must be able to generate identifiers concurrently.

Developers then need to make a choice between generating the identifiers either at the time of creation of the
business documents, or after the documents have been generated.  This in turn leads to the following possible
technical concerns:

1. **Uniqueness**: Each generated identifier must be unique.
1. **Sequential**: The generated identifiers must be sequential in nature within a given time period.
1. **No gaps**: There must not be any gaps in the generated identifiers.
1. **Concurrency**: Multiple application users must be able to generate identifiers concurrently.
1. **Identifier generation at document creation**: The identifiers must be generated at the time of creation of
the business document.
1. **No locking**: The identifiers must be generated without any significant locking on a common resource, such
as a database table.

Many relational database systems like MySQL and Microsoft SQL Server provide auto-generated identifiers.
However, these are not guaranteed to always be in sequence.  This is because values for such identifiers generated
during rolled back database operations are not reclaimed, thereby leaving gaps in in the identifier values.

Similarly, support for multiple concurrent users requires that some sort of database lock be obtained so that each
user generates a unique identifier.  The locks must be obtained for a very short period of time, otherwise multiple
requests are likely to queue up fast and lead to either degraded application performance or even an application
crash if too many requests queue up too fast.

In general, at least one of the 6 technical concerns listed above must be sacrificed in order to develop a
practical software system.  It is technically impossible to meet all these concerns and still achieve the desired
functionality consistently and reliably.

# Overview
----

Relational database systems offer the concept of `Transaction Isolation` for determining how concurrent users are
able to view data being accessed or modified by a transaction.  `Transaction Isolation` is the **I** in `ACID`
properties of relational databases.  Lower isolation levels guarantee higher levels of concurrency by reducing the
need to obtain locks.  However, this sacrifices uniqueness as concurrent users may end up generating the same
identifier value.  Higher isolation levels guarantee higher levels of uniqueness at the expense of greater locking
and reduced performance.

This application attempts to test the behaviour of various relational database systems with different isolation
levels.  `H2`, `MySQL` and `Oracle` are supported out of the box and more can be added by changing the application
configuration.  The effect of transaction isolation level can be checked by changing the code in the class named
`BillingService`.

# Running the application
----

This application uses Maven as the build tool.  After checking out the code, the application should be run against
one of the supported databases as described below.  Then, use a load generation tool like
[Apache JMeter](http://jmeter.apache.org) to simulate some concurrent user load sending requests to
`http://localhost`.  Watch the application log messages printed out to the system console.

### Test against H2
[H2](http://www.h2database.com) is an in-memory database.  The application can be run against H2 by
running as `mvn clean tomcat7:run -Ph2`.

### Test against MySQL
First, change the MySQL database connection information in the file `pom.xml`.  Then run the application against
[MySQL](http://www.mysql.com) as `mvn clean tomcat7:run -Pmysql`.

### Test against Oracle
Before running the application against Oracle, install an Oracle JDBC driver in the local Maven repository (use Google
to search for instructions on doing this).  This is required because the Oracle JDBC driver is not available in any of
the public Maven repositories due to Oracle's restrictions on distribution of binary files.  Then, change the Oracle
database connection information in the file `pom.xml`.  Finally, run the application as `mvn clean tomcat7:run -Poracle`.

### Test against other databases
Add a Maven profile for the required database in the file `pom.xml`, supply the necessary database connection information
and then run the application with the appropriate Maven profile.


# Findings
----

Information below applies to test runs against H2, MySQL and Oracle databases with a concurrent load of 10 users that keep
on hammering the application without interruption for a period of 5 minutes.

### Summary
Of all the tested isolation levels, `SERIALIZABLE` meets the most requirements, but at the expense of concurrency.  This being
the highest isolation level, results in the greatest amount of locking and therefore results in the least number of
concurrent users supported.

MySQL provided the best results (surprisingly).  Using the `SERIALIZABLE` isolation level with MySQL generated occasional
`CannotAcquireLockException`, which was expected because transactions get queued with this isolation level.  No non-unique
values were generated.  Using `SERIALIZABLE` with H2 did not throw up any instance of `CannotAcquireLockException` but instead
generated non-unique values, which was surprising.  Oracle (Express Edition 11g) threw up both `CannotAcquireLockException` and
non-unique values, again a big surprise.

### Run against H2

|                        | READ_UNCOMMITTED | READ_COMMITTED | REPEATABLE_READ | SERIALIZABLE |
| ---------------------- | ---------------- | -------------- | --------------- | ------------ |
| Uniqueness             | No#              | No#            | No#             | No#          |
| Sequential             | No               | Yes            | Yes             | Yes          |
| No gaps                | No               | Yes            | Yes             | Yes          |
| Concurrency            | No               | No*            | No*             | No*          |
| Identifier at creation | Yes              | Yes            | Yes             | Yes          |
| No locking             | Yes              | No             | No              | No           |

```
# Non-unique values are generated.  Unique values can be forced by applying a unique constraint on the database table columns
but then the application throws DataIntegrityViolationException under concurrent load.
* Concurrency can be enforced by retrying the transaction.
```

### Run against MySQL

|                        | READ_UNCOMMITTED | READ_COMMITTED | REPEATABLE_READ | SERIALIZABLE |
| ---------------------- | ---------------- | -------------- | --------------- | ------------ |
| Uniqueness             | No#              | No#            | No#             | Yes          |
| Sequential             | No               | Yes            | Yes             | Yes          |
| No gaps                | No               | Yes            | Yes             | Yes          |
| Concurrency            | No               | No*            | No*             | No*          |
| Identifier at creation | Yes              | Yes            | Yes             | Yes          |
| No locking             | Yes              | No             | No              | No           |

```
# Non-unique values are generated.  Unique values can be forced by applying a unique constraint on the database table columns
but then the application throws DataIntegrityViolationException under concurrent load.
* Concurrency can be enforced by retrying the transaction.
```

### Run against Oracle

|                        | READ_UNCOMMITTED | READ_COMMITTED | REPEATABLE_READ | SERIALIZABLE |
| ---------------------- | ---------------- | -------------- | --------------- | ------------ |
| Uniqueness             | No#              | No#            | No#             | No#          |
| Sequential             | No               | Yes            | Yes             | Yes          |
| No gaps                | No               | Yes            | Yes             | Yes          |
| Concurrency            | No               | No*            | No*             | No*          |
| Identifier at creation | Yes              | Yes            | Yes             | Yes          |
| No locking             | Yes              | No             | No              | No           |

```
# Non-unique values are generated.  Unique values can be forced by applying a unique constraint on the database table columns
but then the application throws DataIntegrityViolationException under concurrent load.
* Concurrency can be enforced by retrying the transaction.
```

# Conclusion
----

It seems that generating sequential identifiers without gaps in a concurrent environment is not an easy task by any means and is
certainly not supported out-of-the-box by any relational database system under reasonably common circumstances.  Even the `SERIALIZABLE`
isolation level does not help because it severely hampers concurrency.

The important requirements of sequential identifiers without gaps can be met even with `READ_COMMITTED` isolation level.  This
at least preserves the performance and concurrency.

Retrying database operations seems to be the only way to ensure that all operations finish reliably.
