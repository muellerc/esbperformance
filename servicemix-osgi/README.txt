Apache ServiceMix 4.5.0 - powered by Camel

Use Maven 3.x to build the artifacts as:

    $ cd ~/resources/esbperformance/servicemix-osgi
    $ mvn install

Increase heap memory by editing ${SMX_HOME}/bin/servicemix to read as follows

    JAVA_MIN_MEM=2048M
    JAVA_MAX_MEM=2048M
    
Copy the configuration files from servicemix-osgi/etc into ${SMX_HOME}/etc
    This reduce the number of features which are started by default.
    Rename ${SMX_HOME}/etc/activemq-broker.xml to activemq-broker.xml.off because we don't need it for the performance test
    Make sure you remove the ${SMX_HOME}/data directory if you already started SMX before.

Start ServiceMix as

    $ cd ${SMX_HOME}/bin
    $ ./servicemix

First install the DirectProxy deployable artifacts into ServiceMix. On the ServiceMix console,
type following to install and start each proxy services into ServiceMix

    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/proxy/1.1.0
 	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/cbr/1.1.0
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/soap-hbr/1.1.0
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/http-hbr/1.1.0
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/xslt/1.1.0
	karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.servicemix-osgi/secure-proxy/1.1.0
    
Before executing the performance test, re-generate the WS-Security related requests as ServiceMix is particular about the timestamps used
    $ cd client-scripts
    $ ./recreate-secure-requests.sh

Execute the performance test as follows

    $ cd ~/client-scripts
    $ ./loadtest.sh http://localhost:8192/service > servicemix-4.4.2.txt


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

