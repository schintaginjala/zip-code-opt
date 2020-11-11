package com.wis.zip.code.opt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZipCodeRangeOptimiser {

    
    /**
     * This method is used to get optimized Zip code boundaries, expected input example: [94133,94133] [94200,94299] [94600,94699] 
     * @param zipCodeboundarySets
     * @return zip code boundary set(s) in String format
     * @throws ZipCodeServiceException 
     */
	public String getOptimizeZipBoundary(String zipCodeboundarySets) throws ZipCodeServiceException {
		if (!isInputValid(zipCodeboundarySets)) {
			throw new ZipCodeServiceException(
					"Invalid input, valid input example: [94133,94133][94200,94299][94600,94699]");
		}
		String[] arrayOfZipcodeBoundaries = zipCodeboundarySets.split("]");
		List<ZipCode> zipList = new ArrayList<>();
		ZipCode zipCode = null;
		for (String zipCodeBoundary : arrayOfZipcodeBoundaries) {
			String[] lowerAndUpperBoundary = zipCodeBoundary.replace("[", "").split(",");
			zipCode = new ZipCode();
			if (lowerAndUpperBoundary[0].length() != 5 || lowerAndUpperBoundary[1].length() != 5) {
				throw new ZipCodeServiceException("Zip code must be 5 digit number only");
			}
			try {
				zipCode.setLowerLimit(Integer.parseInt(lowerAndUpperBoundary[0]));
				zipCode.setUpperLimit(Integer.parseInt(lowerAndUpperBoundary[1]));
			} catch (NumberFormatException e) {
				throw new ZipCodeServiceException("invalid zip code");
			}
			zipList.add(zipCode);
		}
		Collections.sort(zipList);
		List<ZipCode> zipcodeOutputList = getOptimizeValues(zipList);
		StringBuilder outputBuilder = new StringBuilder();
		for (ZipCode zipCodeOutput : zipcodeOutputList) {
			outputBuilder.append("[").append(zipCodeOutput.getLowerLimit()).append(",")
					.append(zipCodeOutput.getUpperLimit()).append("]");
		}
		return outputBuilder.toString();
	}

    private boolean isInputValid(String zipCodeBoundarySets) {
    	boolean isInputValid = true;
    	if (zipCodeBoundarySets == null) {
    		isInputValid = false;
    	} else if (zipCodeBoundarySets.length() == 0) {
    		isInputValid = false;
    	} else if(zipCodeBoundarySets.length() % 13 != 0){
    		isInputValid = false;
    	} else if (!zipCodeBoundarySets.substring(0, 1).equals("[")) {
    		isInputValid = false;
		} else {
    		for (int i = 12; i<zipCodeBoundarySets.length(); i=i+13) {
    			if (!zipCodeBoundarySets.subSequence(i, i+1).equals("]")) {
    				isInputValid = false;
    				break;
    			}
    			if (zipCodeBoundarySets.length()!=i+1 && !zipCodeBoundarySets.subSequence(i+1, i+2).equals("[")) {
    				isInputValid = false;
    				break;
    			}
    		}    		
    	}
    	return isInputValid;
    }

	public static List<ZipCode> getOptimizeValues(List<ZipCode> zipList) {
		List<ZipCode> optList = new ArrayList<>();
		if (null != zipList && !zipList.isEmpty()) {
			ZipCode zipPrev = zipList.get(0);
			for (int i = 1; i < zipList.size(); i++) {
				ZipCode zipNext = zipList.get(i);
                /**
                 * There could be the below scenarios
                 * 1. if previous zip code lower limit is less than or equal to the next zip code lower limit and
                 *     if previous zip code upper limit is greater than or equal to the next zip code
                 *     then this case need to ignore to store into the zip code, since this will cover win prev zip code list
                 * 2. If prevZip code lower limit is less than the next zip code lower limit and
                 *    if the prevZip upper limit is greater than next zip code of lower limit
                 *      then replace the upper limit of previous zip code and continue
                 * 3. If the prevZip lower limit is less than or equal to next zip of lower limit and
                 *     if the prevZip of upper limit is less than next zip of lower limit then
                 *       add prevZip into list
                 *       assign prevZip with next zip
                 */
				if (zipPrev.getLowerLimit() <= zipNext.getLowerLimit()
						&& zipPrev.getUpperLimit() >= zipNext.getUpperLimit()) {
					continue;
				} else if (zipPrev.getLowerLimit() <= zipNext.getLowerLimit()
						&& zipPrev.getUpperLimit() > zipNext.getLowerLimit()) {
					zipPrev.setUpperLimit(zipNext.getUpperLimit());
				} else if (zipPrev.getLowerLimit() <= zipNext.getLowerLimit()
						&& zipPrev.getUpperLimit() < zipNext.getUpperLimit()) {
					optList.add(zipPrev);
					zipPrev = zipNext;
				}
			}
			optList.add(zipPrev);
		}
		return optList;
	}
}

class ZipCode implements Comparable<ZipCode> {
    private int upperLimit;
    private int lowerLimit;

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    @Override
    public int compareTo(ZipCode zip) {
        return new Integer(this.getLowerLimit()).compareTo(zip.getLowerLimit());
    }

    @Override
    public String toString() {
        return "[" + lowerLimit +
                "," + upperLimit +
                "]";
    }
}