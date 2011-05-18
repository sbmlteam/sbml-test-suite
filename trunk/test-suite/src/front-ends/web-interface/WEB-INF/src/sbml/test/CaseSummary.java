package sbml.test;

import java.math.*;
import java.util.*;


public class CaseSummary
{
    // 
    // --------------------------- Public methods ----------------------------- 
    // 

    public CaseSummary(String caseName)
    {
        this.caseName = caseName;
        this.caseNum  = Integer.parseInt(caseName);
    }

    public String getCaseName() { return caseName; }
    public int    getCaseNum()  { return caseNum; }

    public void setLevelsAndVersions(Vector<String> levelsAndVersions)
    {
        this.levelsAndVersions.addAll(levelsAndVersions);
    }

    public void setLevelsAndVersions(String[] levelsAndVersions)
    {
        for (String lv : levelsAndVersions)
            this.levelsAndVersions.add(lv);
    }

    public boolean hasLevelAndVersion(String lv)
    {
        return levelsAndVersions.contains(lv);
    }

    public void setTags(Vector<String> tags, Vector<String> allTags)
    {
        this.tagStrings = tags;
        this.numBits    = allTags.size();
        this.tagBits    = new TagBits(tags, allTags);
        this.tagCode    = tagBits.longValue(numBits);
    }

    public boolean caseHasTag(String tag)
    {
        return tagStrings.contains(tag);
    }

    public boolean caseHasTags(String[] tags)
    {
        for (String tag : tags)
            if (! tagStrings.contains(tag))
                return false;
        return true;
    }

    public Vector<String> getCaseTags()
    {
        return this.tagStrings;
    };
    
    public TagBits getTagBits()
    {
        return tagBits;
    }

    public long getTagCode()
    {
        return tagCode;
    }

    // 
    // -------------------------- Private variables ---------------------------
    // 

    private Vector<String> levelsAndVersions = new Vector<String>();
    private Vector<String> tagStrings = new Vector<String>();
    private TagBits tagBits;
    private long tagCode;
    private String caseName;
    private int caseNum;
    private int numBits;

}// end of class
