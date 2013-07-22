OpenNMS-NG
==========
--
What is OpenNMS-NG?
-------------------
OpenNMS-NG is a modular imlementation of the OpenNMS monitoring stack based on OSGi.  It is seriously a work in progress.

OpenNMS-NG runs in Karaf and its goal is to allow daemons and components to run in a distributed fashion.  The idea is to provide persistent messaging as the backbone and allow for consumer-based event polling for obtaining messages.  By implementing this style of architecture, events can be handled on multiple machine utilizing a competing consumer methodology.  This allows OpenNMS to become fault tolerant with no single point of failure, and also allows it to scale massively in a horizontal capacity, theoretically allowing it to handle any load or any sized datacenter(s).  This also lends itself well to cloud deployments and ability to scale based on load.


