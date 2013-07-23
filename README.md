OpenNMS-NG
==========
--
What is OpenNMS-NG?
-------------------
OpenNMS-NG is a modular imlementation of the OpenNMS monitoring stack based on OSGi.  It is seriously a work in progress.

OpenNMS-NG runs in Karaf and its goal is to allow daemons and components to run in a distributed fashion.  The idea is to provide persistent messaging as the backbone and allow for consumer-based event polling for obtaining messages.  By implementing this style of architecture, events can be handled on multiple machine utilizing a competing consumer methodology.  This allows OpenNMS to become fault tolerant with no single point of failure, and also allows it to scale massively in a horizontal capacity, theoretically allowing it to handle any load or any sized datacenter(s).  This also lends itself well to cloud deployments and ability to scale based on load.

To use OpenNMS-NG you will need the following:

- karaf (we recommend the <a href="https://github.com/savoirtech/aetos">aetos</a> karaf stack which is a good solid version of karaf with the necessary dependencies).  Which ever version of Karaf that you choose to utilize, it requires v2.3.1 or later.
- <a href="http://activemq.apache.org">ActiveMQ</a> 5.7 or later
- <a href="http://www.postgresql.org/">PostgreSQL</a>
- <a href="http://www.opennms.org/">OpenNMS</a>

Building OpenNMS-NG
-------------------
Building OpenNMG-NG requires maven and Java.  First do a <code>git clone https://github.com/savoirtech/opennms-ng.git</code> and go into the directory once checked out.  Issue a <code>mvn clean install</code> and it should build the software.

Preparing the OpenNMS-NG runtime
--------------------------------
OpenNMS-NG requires that OpenNMS, ActiveMQ, PosgreSQL, and Karaf(aetos) are all available.   It is assumed that OpenNMS, PostgreSQL, and ActiveMQ will be running on the same machine as OpenNMS-NG.  This can be changed through the etc files to make the deployment more distibuted.  But for the sake of getting it running, we will assume it is all on the same machine.

### OpenNMS Installation ###
The installation of OpenNMS is beyond the scope of this document and you should be familar with how to get it installed.  The reason for its installation if for its configuration files and the database schema.  Information for how to install OpenNMS can be found <a href="http://www.opennms.org/documentation/installguide.html">here</a>.  Typically OpenNMS will be installed at /opt/opennms.  OpenNMS *does not* and *should not* be runnining when it comes time to execute OpenNMS-NG.  But we do need to get it to install the PostgreSQL database schema and have its configuration files available.  Once the database has been installed into PostgreSQL, you may shut down OpenNMS.

### ActiveMQ Installation ###
Download a version of ActiveMQ from <a href="http://activemq.apache.org/download.html">here</a>. You may follow the instructions for installing and running ActiveMQ can be found <a href="http://activemq.apache.org/getting-started.html">here</a>.  Follow these instuctions to get ActiveMQ running.

### Aetos or Karaf installation ###
We recommend that you use aetos since it is a karaf build with all of the necessary dependencies in it.  You may checkout the latest version (master) of aetos from <a href="https://github.com/savoirtech/aetos">here</a>.  Build it with the usual <code>mvn clean install</code> at the root aetos directory.  When it has completed building, there will be a tar.gz bunlde underneath the $AETOS_ROOT/aetos/target directory named something like "aetos-X.X-SNAPSHOT.tar.gz".  Retrieve that bundle and expand it somewhere.  The expnded version is your custom karaf build (aetos).

If you choose to use karaf, you may download it from the <a href="http://karaf.apache.org">Karaf website</a>.  Just be sure the version you use is 2.3.1 or later.

From this point forward aetos and karaf will be referred to as "karaf".

### Getting Karaf configuration set up ###
Once karaf has been expanded, we need to do a softlink to the OpenNMS installation under the karaf home directroy.  <code>cd</code> into the karaf directory to setup the softlink.  Assuming OpenNMS was installed in the default location of /opt/opennms, you would then issue the command:

<code>ln -s /opt/opennms opennms</code>

Doing a <code>ls</code> should show that it was created.

Next, we need to edit the system.properties so the JVM can get a handle on where the OpenNMS home directory is, which happens to be the softlink that we just created.   Edit the <code>$KARAF_HOME/etc/system.properties</code> and at the bottom of the file, add the following:

<code>opennms.home=${karaf.home}/opennms</code>

Finally, in order to make it more simple to run our features without continually having to add the OpenNMS-NG feature url to karaf, we will add the url to the features list, which will load the list when karaf boots.  Edit the <code>$KARAF_HOME/etc/org.apache.karaf.features.cfg</code> and at the end of the <code>featuresRepositories</code properties file, we need to add a comma after the last <code>mvn:org...</code> line and add the following url:

<code>mvn:org.opennms.ng/features/1.0-SNAPSHOT/xml/features</code>

At this point, OpenNMS-NG should be ready to deploy

### Running and Deploying OpenNMS-NG ###
To deploy OpenNMS-NG modules, you must have the following running on your machine:

1. ActiveMQ
2. PostgreSQL (with the OpenNMS schema installed)
3. Karaf

If you do not know how to run these products, please refer to those project's websites for more information.

The available OpenNMS-NG features can be found in the <code>opennms-ng</code> directory.  To deploy a daemon, such as eventd, we would do the following. At the karaf prompt, type in:

<code>features:install eventd</code>

If everything was set up and install correctly, you should see the following:


