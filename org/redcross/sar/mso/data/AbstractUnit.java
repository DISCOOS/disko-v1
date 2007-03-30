package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.ArrayList;

public abstract class AbstractUnit extends AbstractMsoObject implements IUnitIf
{
    private final AttributeImpl.MsoString m_callSign = new AttributeImpl.MsoString(this, "CallSign");
    private final AttributeImpl.MsoLong m_number = new AttributeImpl.MsoLong(this, "Number");

    private final AssignmentListImpl m_assignmentList = new AssignmentListImpl(this, "UnitAssignment", false);
    private final MsoReferenceImpl<IHierarchicalUnitIf> m_superior = new MsoReferenceImpl<IHierarchicalUnitIf>(this, "UnitSuperior", false);

    public AbstractUnit(IMsoObjectIf.IObjectIdIf anObjectId, Long aNumber)
    {
        super(anObjectId);
        m_number.setValue(aNumber);
    }

    public boolean setSuperior(IHierarchicalUnitIf aSuperior)
    {
        IHierarchicalUnitIf tu = aSuperior;
        while (tu != null && tu != this)
        {
            tu = tu.getSuperior();
        }
        if (tu != null)
        {
            return false;
        }
        m_superior.setReference(aSuperior);
        return true;
    }

    public IHierarchicalUnitIf getSuperior()
    {
        return m_superior.getReference();
    }

    /**
     * Generate list of subordinates
     *
     * @return The list
     */
    public ArrayList<IHierarchicalUnitIf> getSubOrdinates()
    {
        return getSubOrdinates(this);
    }

    public static ArrayList<IHierarchicalUnitIf> getSubOrdinates(IHierarchicalUnitIf aUnit)
    {
        ArrayList<IHierarchicalUnitIf> resultList = new ArrayList<IHierarchicalUnitIf>();
        IUnitListIf mainList = MsoModelImpl.getInstance().getMsoManager().getCmdPost().getUnitList();
        for (IUnitIf u : mainList.getItems())
        {
            if (u.getSuperior() == aUnit) // todo Test !!!!!!!!!!!!!!!!
            {
                resultList.add(u);
            }
        }
        return resultList;
    }

    public String toString()
    {
        return "AbstractUnit" + " " + getObjectId();
    }

    public void addAssignment(IAssignmentIf anAssignment) throws DuplicateIdException
    {
        m_assignmentList.add(anAssignment);
    }

    public void removeAssignment(IAssignmentIf anAssignment)
    {
        m_assignmentList.removeReference(anAssignment);
    }

    public int assignmentCount()
    {
        return m_assignmentList.size();
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT;
    }

    protected void defineAttributes()
    {
        addAttribute(m_callSign);
        addAttribute(m_number);
    }

    protected void defineLists()
    {
        addList(m_assignmentList);
    }

    protected void defineReferences()
    {
        addReference(m_superior);
    }

    public void setCallSign(String aCallsign)
    {
        m_callSign.setValue(aCallsign);
    }

    public String getCallSign()
    {
        return m_callSign.getString();
    }

}