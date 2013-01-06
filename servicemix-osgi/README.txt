Apache ServiceMix 4.5.0 - powered by Camel


SETUP
=====

Use Maven 3.x to build the artifacts as:
    $ cd ~/resources/esbperformance/servicemix-osgi
    $ mvn install

Increase heap memory by editing ${SMX_HOME}/bin/servicemix to read as follows:
    JAVA_MIN_MEM=2048M
    JAVA_MAX_MEM=2048M

Rename ${SMX_HOME}/etc/activemq-broker.xml to ${SMX_HOME}/etc/activemq-broker.xml.disabled (we do not need/use ActiveMQ)
Copy servicemix-osgi/etc/* into ${SMX_HOME}/etc
Copy servicemix-osgi/patch/apache-camel-2.10.3-features.xml into ${SMX_HOME}/system/org/apache/camel/karaf/apache-camel/2.10.3/ (this fixed upgrade to CXF 2.6.4)
Copy servicemix-osgi/patch/apache-cxf-2.6.4-features.xml into ${SMX_HOME}/system/org/apache/cxf/karaf/apache-cxf/2.6.4/ (this fixed some required dependency and upgrade to wss4j 1.6.9)
Edit ${SMX_HOME}/etc/jetty.xml:
    Replace the '8' in '<Set name="Acceptors">8</Set>' with the number of available cores (e.g. '2' for an Intel Core 2 Duo)

Make sure you remove the ${SMX_HOME}/data directory if you already started SMX before!



INSTALLATION
============

Start ServiceMix as:
    $ cd ${SMX_HOME}/bin
    $ ./servicemix

First install the service deployable artifacts into ServiceMix. On the ServiceMix console,
type following commands to install and start each proxy services into ServiceMix:
    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/proxy/2.0.0-SNAPSHOT
 	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/cbr/2.0.0-SNAPSHOT
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/soap-hbr/2.0.0-SNAPSHOT
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/http-hbr/2.0.0-SNAPSHOT
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/xslt/2.0.0-SNAPSHOT
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/secure-proxy/2.0.0-SNAPSHOT
    
Before executing the performance test, re-generate the WS-Security related requests as ServiceMix is particular about the timestamps used:
    $ cd ${ULTRA_ESB_HOME}/samples/bin/
    $ ./recreate-secure-requests.sh (they are generated into ${ULTRA_ESB_HOME}/samples/bin/resources/requests)


EXECUTION
=========

Execute the performance test as follows:
    $ cd ${ULTRA_ESB_HOME}/samples/bin/
    $ ./loadtest.sh http://localhost:8192/service > servicemix-4.5.0.txt



Service URLs
============

Direct Proxy
    url             : http://localhost:8192/service/DirectProxy
    wsdl-url        : http://localhost:8192/service/DirectProxy?wsdl

SOAP Body CBR Proxy
    url             : http://localhost:8192/service/CBRProxy
    wsdl-url        : http://localhost:8192/service/CBRProxy?wsdl

SOAP Header CBR Proxy
    url             : http://localhost:8192/service/CBRSOAPHeaderProxy
    wsdl-url        : http://localhost:8192/service/CBRSOAPHeaderProxy?wsdl

Transport Header CBR Proxy
    url             : http://localhost:8192/service/CBRTransportHeaderProxy
    wsdl-url        : http://localhost:8192/service/CBRTransportHeaderProxy?wsdl

XSLT Transformation Proxy
    url             : http://localhost:8192/service/XSLTProxy
    wsdl-url        : http://localhost:8192/service/XSLTProxy?wsdl

WS-Security Proxy
    url             : http://localhost:8192/service/SecureProxy
    wsdl-url        : http://localhost:8192/service/SecureProxy?wsdl