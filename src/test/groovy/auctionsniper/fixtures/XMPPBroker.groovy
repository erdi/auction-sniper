package auctionsniper.fixtures

import org.apache.vysper.mina.TCPEndpoint
import org.apache.vysper.storage.inmemory.MemoryStorageProviderRegistry
import org.apache.vysper.xmpp.addressing.EntityImpl
import org.apache.vysper.xmpp.authorization.AccountManagement
import org.apache.vysper.xmpp.server.XMPPServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class XMPPBroker implements TestRule {

    public static final String BOGUS_CERTIFICATE_PASSWORD = "boguspw"
    public static final String BOGUS_CERTIFICATE_PATH = "/bogus_mina_tls.cert"
    private final List<String> userNames
    public final static String DOMAIN = "growing.org"

    XMPPBroker(List<String> userNames) {
        this.userNames = userNames
    }

    @Override
    Statement apply(Statement base, Description description) {
        return {
            def server = configureServer()
            server.start()
            try {
                base.evaluate()
            } finally {
                server.stop()
            }
        }
    }

    private MemoryStorageProviderRegistry providerRegistryWithUsers() {
        def providerRegistry = new MemoryStorageProviderRegistry()
        AccountManagement accountManagement = providerRegistry.retrieve(AccountManagement)
        userNames.each {
            accountManagement.addUser(EntityImpl.parse("$it@$DOMAIN"), it)
        }
        providerRegistry
    }

    XMPPServer configureServer() {
        def server = new XMPPServer(DOMAIN)
        server.addEndpoint(new TCPEndpoint())
        server.storageProviderRegistry = providerRegistryWithUsers()
        server.setTLSCertificateInfo(bogusCertificateStream(), BOGUS_CERTIFICATE_PASSWORD)
        server
    }

    private InputStream bogusCertificateStream() {
        getClass().getResourceAsStream(BOGUS_CERTIFICATE_PATH)
    }
}
