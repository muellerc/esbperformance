Apache Karaf 2.3.0 - powered by Camel

SETUP
=====

Download Apache Karaf 2.3.0 from http://karaf.apache.org/index/community/download.html and unzip/untar the archive.

Use Maven 3.x to build the artifacts as:
    $ cd ~/resources/esbperformance/karaf
    $ mvn install

Increase heap memory by editing ${KARAF_HOME}/bin/karaf to read as follows:
    JAVA_MIN_MEM=2048M
    JAVA_MAX_MEM=2048M
    
Copy karaf/etc/* into ${KARAF_HOME}/etc



INSTALLATION
============

Start Karaf as (make sure you removed ${KARAF_HOME}/data if this directory already exists):
    $ cd ${KARAF_HOME}/bin
    $ ./karaf

First install the DirectProxy deployable artifacts into ServiceMix. On the ServiceMix console,
type following to install and start each proxy services into ServiceMix
    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.karaf/proxy/1.0.0-SNAPSHOT
    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.karaf/cbr/1.0.0-SNAPSHOT
    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.karaf/soap-hbr/1.0.0-SNAPSHOT
    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.karaf/http-hbr/1.0.0-SNAPSHOT
    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.karaf/xslt/1.0.0-SNAPSHOT
    karaf@trun> install -s mvn:org.apache.cmueller.camel.esbperf.karaf/secure-proxy/1.0.0-SNAPSHOT
    
Before executing the performance test, re-generate the WS-Security related requests as Karaf is particular about the timestamps used
    $ cd ${ULTRA_ESB_HOME}/samples/bin/
    $ ./recreate-secure-requests.sh (they are generated into ${ULTRA_ESB_HOME}/samples/resources/requests)



EXECUTION
=========

Execute the performance test as follows
    $ cd ${ULTRA_ESB_HOME}/samples/bin/
    $ ./loadtest.sh http://localhost:8192/service > karaf-2.3.0.txt


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

