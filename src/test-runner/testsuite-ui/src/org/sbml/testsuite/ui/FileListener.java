package org.sbml.testsuite.ui;

import java.io.File;

public interface FileListener
{
    void fileModified();
    void fileCreated();
    void fileDeleted();
    // Compatibility with FileMonitor implementation.
    void fileChanged (File file);
}
