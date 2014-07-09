/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang2sources.plugin.it;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import com.google.common.base.Joiner;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.Test;

public class YangToSourcesPluginTestIT {

    // TODO Test yang files in transitive dependencies

    @Test
    public void testYangRootNotExist() throws URISyntaxException {
        try {
            setUp("test-parent/YangRootNotExist/", false);
        } catch (VerificationException e) {
            assertVerificationException(e,
                    "[ERROR] yang-to-sources: Unable to parse yang files from ");
            assertVerificationException(
                    e,
                    "Caused by: org.apache.maven.plugin.MojoExecutionException: yang-to-sources: Unable to parse yang files from ");
            return;
        }

        fail("Verification exception should have been thrown");
    }

    @Test
    public void testCorrect() throws Exception {
        Verifier v = setUp("test-parent/Correct/", false);
        verifyCorrectLog(v);
    }

    @Test
    public void testAdditionalConfiguration() throws Exception {
        Verifier v = setUp("test-parent/AdditionalConfig/", false);
        v.verifyTextInLog("[DEBUG] yang-to-sources: Additional configuration picked up for : org.opendaylight.yangtools.yang2sources.spi.CodeGeneratorTestImpl: {nm1=abcd=a.b.c.d, nm2=abcd2=a.b.c.d.2}");
        v.verifyTextInLog("[DEBUG] yang-to-sources: Additional configuration picked up for : org.opendaylight.yangtools.yang2sources.spi.CodeGeneratorTestImpl: {c1=config}");
        v.verifyTextInLog(File.separator
                + "files marked as resources: META-INF/yang");
        v.verifyTextInLog(
                Joiner.on(File.separator).join(
                Arrays.asList("target", "generated-sources", "spi"))

                + " marked as resources for generator: org.opendaylight.yangtools.yang2sources.spi.CodeGeneratorTestImpl");
    }

    @Test
    public void testMissingYangInDep() throws Exception {
        try {
            setUp("test-parent/MissingYangInDep/", false);
        } catch (VerificationException e) {
            assertVerificationException(
                    e,
                    "org.opendaylight.yangtools.yang.parser.util.YangValidationException: Not existing module imported:unknownDep:2013-02-27 by:private:2013-02-27");
            return;
        }

        fail("Verification exception should have been thrown");
    }

    @Test
    public void testNamingConflict() throws Exception {
        Verifier v = setUp("test-parent/NamingConflict/", false);
        v.verifyErrorFreeLog();
        String baseDir = v.getBasedir();
        String fileName = v.getLogFileName();
        List<String> lines = v.loadFile(baseDir, fileName, false);
        for (String s : lines) {
            if (s.contains("conflict")) {
                System.err.println(s);
            }
        }
        v.verifyTextInLog("[WARNING] Naming conflict for type 'org.opendaylight.yang.gen.v1.urn.yang.test.rev140303.NetworkTopologyRef': file with same name already exists and will not be generated.");
    }

    static void verifyCorrectLog(Verifier v) throws VerificationException {
        v.verifyErrorFreeLog();
        v.verifyTextInLog("[INFO] yang-to-sources: YANG files parsed from");
        v.verifyTextInLog("[INFO] yang-to-sources: Code generator instantiated from org.opendaylight.yangtools.yang2sources.spi.CodeGeneratorTestImpl");
        v.verifyTextInLog("[INFO] yang-to-sources: Sources generated by org.opendaylight.yangtools.yang2sources.spi.CodeGeneratorTestImpl: null");
    }

    @Test
    public void testNoGenerators() throws Exception {
        Verifier v = setUp("test-parent/NoGenerators/", false);
        v.verifyErrorFreeLog();
        v.verifyTextInLog("[WARNING] yang-to-sources: No code generators provided");
    }

    @Test
    public void testInvalidVersion() throws Exception {
        Verifier v = setUp("test-parent/InvalidVersion/", false);
        v.verifyErrorFreeLog();
        v.verifyTextInLog("[WARNING] yang-to-sources: Dependency resolution conflict:");
    }

    @Test
    public void testUnknownGenerator() throws Exception {
        Verifier v = setUp("test-parent/UnknownGenerator/", true);
        v.verifyTextInLog("[ERROR] yang-to-sources: Unable to generate sources with unknown generator");
        v.verifyTextInLog("java.lang.ClassNotFoundException: unknown");
        v.verifyTextInLog("[INFO] yang-to-sources: Code generator instantiated from org.opendaylight.yangtools.yang2sources.spi.CodeGeneratorTestImpl");
        v.verifyTextInLog("[INFO] yang-to-sources: Sources generated by org.opendaylight.yangtools.yang2sources.spi.CodeGeneratorTestImpl: null");
        v.verifyTextInLog("[ERROR] yang-to-sources: One or more code generators failed, including failed list(generatorClass=exception) {unknown=java.lang.ClassNotFoundException}");
    }

    @Test
    public void testNoYangFiles() throws Exception {
        Verifier v = setUp("test-parent/NoYangFiles/", false);
        v.verifyTextInLog("[INFO] yang-to-sources: No input files found");
    }

    static void assertVerificationException(VerificationException e,
            String string) {
        assertThat(e.getMessage(), containsString(string));
    }

    static Verifier setUp(String project, boolean ignoreF)
            throws VerificationException, URISyntaxException {
        final URL path = YangToSourcesPluginTestIT.class.getResource("/"
                + project + "pom.xml");
        File parent = new File(path.toURI());
        Verifier verifier = new Verifier(parent.getParent());
        if (ignoreF)
            verifier.addCliOption("-fn");
        verifier.setMavenDebug(true);
        verifier.executeGoal("generate-sources");
        return verifier;
    }

    @Test
    public void testNoOutputDir() throws Exception {
        Verifier v = YangToSourcesPluginTestIT.setUp("test-parent/NoOutputDir/", false);
        verifyCorrectLog(v);
    }

    @Test
    public void testFindResourceOnCp() throws Exception {
        Verifier v1 = new Verifier(new File(getClass().getResource(
                "/test-parent/GenerateTest1/pom.xml").toURI()).getParent());
        v1.executeGoal("clean");
        v1.executeGoal("package");

        Properties sp = new Properties();
        try (InputStream is = new FileInputStream(v1.getBasedir() + "/it-project.properties")) {
             sp.load(is);
        }
        String buildDir = sp.getProperty("target.dir");

        v1.assertFilePresent(buildDir + "/classes/META-INF/yang/testfile1.yang");
        v1.assertFilePresent(buildDir + "/classes/META-INF/yang/testfile2.yang");
        v1.assertFilePresent(buildDir + "/classes/META-INF/yang/testfile3.yang");

        Verifier v2 = new Verifier(new File(getClass().getResource(
                "/test-parent/GenerateTest2/pom.xml").toURI()).getParent());
        v2.executeGoal("clean");
        v2.executeGoal("package");

        sp = new Properties();
        try (InputStream is = new FileInputStream(v2.getBasedir() + "/it-project.properties")) {
             sp.load(is);
        }
        buildDir = sp.getProperty("target.dir");

        v2.assertFilePresent(buildDir + "/classes/META-INF/yang/private.yang");
        v2.assertFileNotPresent(buildDir + "/classes/META-INF/yang/testfile1.yang");
        v2.assertFileNotPresent(buildDir + "/classes/META-INF/yang/testfile2.yang");
        v2.assertFileNotPresent(buildDir + "/classes/META-INF/yang/testfile3.yang");
    }

}
