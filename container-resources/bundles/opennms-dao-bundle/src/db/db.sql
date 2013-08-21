@funcs@
SET IGNORECASE TRUE;
SET WRITE_DELAY 1;
SET AUTOCOMMIT FALSE;


CREATE SEQUENCE alarmsNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE catNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE eventsNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE memoNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE nodeNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE notifyNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE opennmsNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE outageNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE serviceNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE SEQUENCE userNotifNxtId
        START WITH 1
        INCREMENT BY 50;
CREATE TABLE alarms (alarmId INTEGER NOT NULL, alarmAckTime TIMESTAMP, alarmAckUser VARCHAR(256), alarmType INTEGER, applicationDN VARCHAR(512), clearKey VARCHAR(255), counter INTEGER NOT NULL, description VARCHAR(4000), eventParms VARCHAR(1024), firstAutomationTime TIMESTAMP, firstEventTime TIMESTAMP, ifIndex INTEGER, ipAddr VARBINARY(20), lastAutomationTime TIMESTAMP, lastEventTime TIMESTAMP, logmsg VARCHAR(1024), managedObjectInstance VARCHAR(512), managedObjectType VARCHAR(512), mouseOverText VARCHAR(64), operinstruct VARCHAR(1024), ossPrimaryKey VARCHAR(512), qosAlarmState VARCHAR(31), reductionKey VARCHAR(255), severity SMALLINT NOT NULL, suppressedTime TIMESTAMP, suppressedUntil TIMESTAMP, suppressedUser VARCHAR(256), tticketId VARCHAR(128), tticketState SMALLINT, eventUEI VARCHAR(256) NOT NULL, x733AlarmType VARCHAR(31), x733ProbableCause INTEGER NOT NULL, dpName VARCHAR(255) NOT NULL, lastEventId INTEGER, nodeId INTEGER, serviceid INTEGER, stickymemo INTEGER, PRIMARY KEY (alarmId), CONSTRAINT U_ALARMS_REDUCTIONKEY UNIQUE (reductionKey));
CREATE TABLE applications (id INTEGER NOT NULL, name VARCHAR(32) NOT NULL, PRIMARY KEY (id), CONSTRAINT U_PPLCTNS_NAME UNIQUE (name));
CREATE TABLE application_service_map (ifserviceid INTEGER, appid INTEGER);
CREATE TABLE assets (id INTEGER NOT NULL, additionalhardware VARCHAR(1), admin VARCHAR(1), assetNumber VARCHAR(64), autoenable VARCHAR(1), building VARCHAR(64), category VARCHAR(64), circuitId VARCHAR(64), comment VARCHAR(1024), connection VARCHAR(32), cpu VARCHAR(1), dateInstalled VARCHAR(64), department VARCHAR(64), description VARCHAR(128), displayCategory VARCHAR(64), division VARCHAR(64), enable VARCHAR(32), floor VARCHAR(64), hdd1 VARCHAR(1), hdd2 VARCHAR(1), hdd3 VARCHAR(1), hdd4 VARCHAR(1), hdd5 VARCHAR(1), hdd6 VARCHAR(1), inputpower VARCHAR(1), userLastModified VARCHAR(20), lastModifiedDate TIMESTAMP, lease VARCHAR(64), leaseExpires VARCHAR(64), maintContractExpires VARCHAR(64), maintcontract VARCHAR(64), managedObjectInstance VARCHAR(512), managedObjectType VARCHAR(512), manufacturer VARCHAR(64), modelNumber VARCHAR(64), notifyCategory VARCHAR(64), numpowersupplies VARCHAR(1), operatingSystem VARCHAR(64), password VARCHAR(32), pollerCategory VARCHAR(64), port VARCHAR(64), rack VARCHAR(64), rackunitheight VARCHAR(2), ram VARCHAR(1), region VARCHAR(64), room VARCHAR(64), serialNumber VARCHAR(64), slot VARCHAR(64), snmpcommunity VARCHAR(1), storagectrl VARCHAR(1), supportPhone VARCHAR(64), thresholdCategory VARCHAR(64), username VARCHAR(32), vendor VARCHAR(64), vendorAssetNumber VARCHAR(64), vendorFax VARCHAR(64), vendorPhone VARCHAR(64), vmwareManagedEntityType VARCHAR(70), vmwareManagedObjectId VARCHAR(70), vmwareManagementServer VARCHAR(70), vmwareState VARCHAR(255), vmwareTopologyInfo VARCHAR(1023), nodeId INTEGER, address1 VARCHAR(256), address2 VARCHAR(256), city VARCHAR(64), country VARCHAR(64), latitude REAL, longitude REAL, state VARCHAR(64), zip VARCHAR(64), PRIMARY KEY (id));
CREATE TABLE atInterface (id INTEGER NOT NULL, ifIndex INTEGER NOT NULL, ipAddr VARCHAR(255) NOT NULL, lastPollTime TIMESTAMP NOT NULL, atPhysAddr VARCHAR(32) NOT NULL, nodeId INTEGER NOT NULL, sourceNodeId INTEGER NOT NULL, status INTEGER NOT NULL, PRIMARY KEY (id));
CREATE TABLE categories (categoryid INTEGER NOT NULL, categoryDescription VARCHAR(255), categoryName VARCHAR(255) NOT NULL, PRIMARY KEY (categoryid), CONSTRAINT U_CTGORIS_CATEGORYNAME UNIQUE (categoryName));
CREATE TABLE category_group (categoryId INTEGER, groupId VARCHAR(64) NOT NULL);
CREATE TABLE category_node (nodeId INTEGER, categoryId INTEGER);
CREATE TABLE distPoller (dpName VARCHAR(255) NOT NULL, dpAdminState INTEGER, dpComment VARCHAR(256), dpDiscLimit NUMERIC, dpIP VARCHAR(16) NOT NULL, dpLastEventPull TIMESTAMP, dpLastNodePull TIMESTAMP, dpLastPackagePush TIMESTAMP, dpRunState INTEGER, PRIMARY KEY (dpName));
CREATE TABLE events (eventId INTEGER NOT NULL, eventAckTime TIMESTAMP, eventAckUser VARCHAR(256), eventAutoAction VARCHAR(256), eventCorrelation VARCHAR(1024), eventCreateTime TIMESTAMP NOT NULL, eventDescr VARCHAR(4000), eventDisplay VARCHAR(1) NOT NULL, eventForward VARCHAR(256), eventHost VARCHAR(256), eventLog VARCHAR(1) NOT NULL, eventLogGroup VARCHAR(32), eventLogMsg VARCHAR(1024), eventMouseOverText VARCHAR(64), eventNotification VARCHAR(128), eventOperAction VARCHAR(256), eventOperActionMenuText VARCHAR(64), eventOperInstruct VARCHAR(1024), eventParms VARCHAR(1024), eventPathOutage VARCHAR(1024), eventSeverity INTEGER NOT NULL, eventSnmp VARCHAR(256), eventSnmpHost VARCHAR(256), eventSource VARCHAR(128) NOT NULL, eventSuppressedCount INTEGER, eventTTicket VARCHAR(128), eventTTicketState INTEGER, eventTime TIMESTAMP NOT NULL, eventUei VARCHAR(256) NOT NULL, ifIndex INTEGER, ipAddr VARBINARY(20), alarmId INTEGER, eventDpName VARCHAR(255) NOT NULL, nodeId INTEGER, serviceId INTEGER, PRIMARY KEY (eventId));
CREATE TABLE ifServices (id INTEGER NOT NULL, lastFail TIMESTAMP, lastGood TIMESTAMP, notify VARCHAR(1), qualifier VARCHAR(16), source VARCHAR(1), status VARCHAR(1), ipInterfaceId INTEGER, serviceId INTEGER, PRIMARY KEY (id));
CREATE TABLE ipInterface (id INTEGER NOT NULL, ipAddr VARCHAR(100), ipHostName VARCHAR(256), ipLastCapsdPoll TIMESTAMP, isManaged VARCHAR(1), DTYPE VARCHAR(255), isSnmpPrimary INTEGER, nodeId INTEGER, snmpInterfaceId INTEGER, PRIMARY KEY (id));
CREATE TABLE memos (id INTEGER NOT NULL, author VARCHAR(255), body VARCHAR(255), created TIMESTAMP, updated TIMESTAMP, type VARCHAR(31), reductionkey VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE node (nodeId INTEGER NOT NULL, nodeCreateTime TIMESTAMP NOT NULL, foreignId VARCHAR(255), foreignSource VARCHAR(255), nodeLabel VARCHAR(256) NOT NULL, nodeLabelSource SMALLINT, lastCapsdPoll TIMESTAMP, nodeDomainName VARCHAR(16), nodeNetBIOSName VARCHAR(16), operatingSystem VARCHAR(64), nodeSysContact VARCHAR(256), nodeSysDescription VARCHAR(256), nodeSysLocation VARCHAR(256), nodeSysName VARCHAR(256), nodeSysOID VARCHAR(256), nodeType SMALLINT, DTYPE VARCHAR(255), dpName VARCHAR(255), nodeParentID INTEGER, PRIMARY KEY (nodeId));
CREATE TABLE notifications (notifyId INTEGER NOT NULL, answeredBy VARCHAR(256), eventUEI VARCHAR(255), interfaceId VARBINARY(20), notifConfigName VARCHAR(255), numericMsg VARCHAR(256), pageTime TIMESTAMP, queueId VARCHAR(256), respondTime TIMESTAMP, subject VARCHAR(256), textMsg VARCHAR(4000) NOT NULL, eventId INTEGER NOT NULL, nodeId INTEGER, serviceId INTEGER, PRIMARY KEY (notifyId));
CREATE TABLE OPENJPA_SEQUENCE_TABLE (ID TINYINT NOT NULL, SEQUENCE_VALUE BIGINT, PRIMARY KEY (ID));
CREATE TABLE outages (outageId INTEGER NOT NULL, ifLostService TIMESTAMP NOT NULL, ifRegainedService TIMESTAMP, suppressTime TIMESTAMP, suppressedBy VARCHAR(255), ifserviceId INTEGER, svcLostEventId INTEGER, svcRegainedEventId INTEGER, PRIMARY KEY (outageId));
CREATE TABLE pathOutage (PATHELEMENT_NODEID INTEGER, criticalPathIp VARCHAR(255), criticalPathServiceName VARCHAR(255));
CREATE TABLE service (serviceId INTEGER NOT NULL, serviceName VARCHAR(255) NOT NULL, PRIMARY KEY (serviceId));
CREATE TABLE snmpInterface (id INTEGER NOT NULL, snmpCollect VARCHAR(255), snmpIfAdminStatus INTEGER, snmpIfAlias VARCHAR(256), snmpIfDescr VARCHAR(256), snmpIfIndex INTEGER, snmpIfName VARCHAR(32), snmpIfOperStatus INTEGER, snmpIfSpeed BIGINT, snmpIfType INTEGER, snmpLastCapsdPoll TIMESTAMP, snmpLastSnmpPoll TIMESTAMP, snmpIpAdEntNetMask VARBINARY(20), snmpPhysAddr VARCHAR(32), snmpPoll VARCHAR(255), nodeId INTEGER, PRIMARY KEY (id));
CREATE TABLE usersNotified (id INTEGER NOT NULL, autoNotify VARCHAR(1), contactInfo VARCHAR(64), media VARCHAR(32), notifyTime TIMESTAMP, userId VARCHAR(256), notifyId INTEGER, PRIMARY KEY (id));
