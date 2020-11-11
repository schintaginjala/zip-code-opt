package com.wis.zip.code.opt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ZipCodeRangeOptimiserTest {

	@Test
	public void testGetOptimizeZipBoundary1() throws ZipCodeServiceException {
		ZipCodeRangeOptimiser codeRangeOptimiser = new ZipCodeRangeOptimiser();
		String optimizedZipcodeBoundaries = codeRangeOptimiser.getOptimizeZipBoundary("[94133,94133][94200,94299][94600,94699]");
		assertEquals("[94133,94133][94200,94299][94600,94699]", optimizedZipcodeBoundaries);		
	}
	
	@Test
	public void testGetOptimizeZipBoundary2() throws ZipCodeServiceException {
		ZipCodeRangeOptimiser codeRangeOptimiser = new ZipCodeRangeOptimiser();
		String optimizedZipcodeBoundaries = codeRangeOptimiser.getOptimizeZipBoundary("[94133,94133][94200,94299][94226,94399]");
		assertEquals("[94133,94133][94200,94399]", optimizedZipcodeBoundaries);		
	}
	
	@Test
	public void testGetOptimizeZipBoundary3() throws ZipCodeServiceException {
		ZipCodeRangeOptimiser codeRangeOptimiser = new ZipCodeRangeOptimiser();
		String optimizedZipcodeBoundaries = codeRangeOptimiser.getOptimizeZipBoundary("[49679,52015][49800,50000][51500,53479][45012,46937][54012,59607][45500,45590][45999,47900][44000,45000][43012,45950]");
		assertEquals("[43012,47900][49679,53479][54012,59607]", optimizedZipcodeBoundaries);		
	}
	
	@Test(expected = ZipCodeServiceException.class)
	public void testGetOptimizeZipBoundaryWithInvalidInput() throws ZipCodeServiceException {
		ZipCodeRangeOptimiser codeRangeOptimiser = new ZipCodeRangeOptimiser();
		codeRangeOptimiser.getOptimizeZipBoundary("[49679,52015][49800,50000");
	}
	
	@Test(expected = ZipCodeServiceException.class)
	public void testGetOptimizeZipBoundaryWithNot5DigitCode() throws ZipCodeServiceException {
		ZipCodeRangeOptimiser codeRangeOptimiser = new ZipCodeRangeOptimiser();
		codeRangeOptimiser.getOptimizeZipBoundary("[49679,52015][4980,50000]");
	}
	
	@Test(expected = ZipCodeServiceException.class)
	public void testGetOptimizeZipBoundaryWithInvalidZipCode() throws ZipCodeServiceException {
		ZipCodeRangeOptimiser codeRangeOptimiser = new ZipCodeRangeOptimiser();
		codeRangeOptimiser.getOptimizeZipBoundary("[49679,5abc5][4980,50000]");
	}
}
