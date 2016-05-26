package xyz.ansidev.simple_crud.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class CustomStringUtilsTests {

	@Test
	public void testSplitCamelCase() {
		assertEquals("Lowercase", CustomStringUtils.splitCamelCase("lowercase"));
		assertEquals("Class", CustomStringUtils.splitCamelCase("Class"));
		assertEquals("My Class", CustomStringUtils.splitCamelCase("MyClass"));
		assertEquals("HTML", CustomStringUtils.splitCamelCase("HTML"));
		assertEquals("PDF Loader", CustomStringUtils.splitCamelCase("PDFLoader"));
		assertEquals("A String", CustomStringUtils.splitCamelCase("AString"));
		assertEquals("Simple XML Parser", CustomStringUtils.splitCamelCase("SimpleXMLParser"));
		assertEquals("GL 11 Version", CustomStringUtils.splitCamelCase("GL11Version"));
	}

}
