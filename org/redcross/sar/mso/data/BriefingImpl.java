package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class BriefingImpl extends AbstractMsoObject implements IBriefingIf
{
    private final ForecastListImpl m_forecastList = new ForecastListImpl(this, "BriefingForecast", false);
    private final EnvironmentListImpl m_environmentList = new EnvironmentListImpl(this, "BriefingEnvironment", false);
    private final SubjectListImpl m_subjectList = new SubjectListImpl(this, "BriefingSubject", false);
    private final MsoReferenceImpl<HypothesisImpl> m_hypothesis = new MsoReferenceImpl<HypothesisImpl>(this, "BriefingHypothesis", true);

    public BriefingImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
    }

    protected void defineLists()
    {
        addList(m_forecastList);
        addList(m_environmentList);
        addList(m_subjectList);
    }

    protected void defineReferences()
    {
        addReference(m_hypothesis);
    }

    public static BriefingImpl implementationOf(IBriefingIf anInterface) throws MsoCastException
    {
        try
        {
            return (BriefingImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to BriefingImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_BRIEFING;
    }


}