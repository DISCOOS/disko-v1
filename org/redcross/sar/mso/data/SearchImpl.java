package org.redcross.sar.mso.data;

/**
 *
 */
public class SearchImpl extends AssignmentImpl implements ISearchIf
{
    private final MsoReferenceImpl<AreaImpl> m_searchArea = new MsoReferenceImpl<AreaImpl>(this, "SearchArea", true);

    public SearchImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }


    protected void defineAttributes()
    {
        super.defineAttributes();
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
        addReference(m_searchArea);
    }
}
