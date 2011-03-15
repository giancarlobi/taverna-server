package org.taverna.server.master.common;

import static org.taverna.server.master.common.Namespaces.SERVER;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.taverna.server.master.TavernaServerImpl;

/**
 * The type of an element that declares the version of the server that produced
 * it.
 * 
 * @author Donal Fellows
 */
@XmlType(namespace = SERVER)
public abstract class VersionedElement {
	/** What version of server produced this element? */
	@XmlAttribute(namespace = SERVER)
	public String serverVersion;
	/** What revision of server produced this element? Derived from SCM commit. */
	@XmlAttribute(namespace = SERVER)
	public String serverRevision;
	/** When was the server built? */
	@XmlAttribute(namespace = SERVER)
	public String serverBuildTimestamp;
	static final String VERSION, REVISION, TIMESTAMP;
	static {
		Properties p = new Properties();
		InputStream is = null;
		try {
			p.load(is = VersionedElement.class
					.getResourceAsStream("/version.properties"));
		} catch (IOException e) {
			TavernaServerImpl.log.warn("failed to read /version.properties", e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				TavernaServerImpl.log.warn("failed to close channel", e);
			}
		}
		VERSION = p.getProperty("tavernaserver.version", "unknownVersion");
		REVISION = p.getProperty("tavernaserver.revision", "unknownRevision")
				+ " (branch: " + p.getProperty("tavernaserver.branch", "trunk")
				+ ")";
		TIMESTAMP = p
				.getProperty("tavernaserver.timestamp", "unknownTimestamp");
	}

	public VersionedElement() {
	}

	protected VersionedElement(boolean ignored) {
		serverVersion = VERSION;
		serverRevision = REVISION;
		serverBuildTimestamp = TIMESTAMP;
	}
}