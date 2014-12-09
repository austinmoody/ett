package gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.edge
import gov.nist.healthcare.ttt.database.xdr.XDRRecordInterface
import gov.nist.healthcare.ttt.database.xdr.XDRTestStepInterface
import gov.nist.healthcare.ttt.tempxdrcommunication.artifact.ArtifactManagement
import gov.nist.healthcare.ttt.webapp.xdr.core.TestCaseExecutor
import gov.nist.healthcare.ttt.webapp.xdr.domain.MsgLabel
import gov.nist.healthcare.ttt.webapp.xdr.domain.TestCaseBuilder
import gov.nist.healthcare.ttt.webapp.xdr.domain.TestCaseEvent
import gov.nist.healthcare.ttt.webapp.xdr.domain.UserMessage
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCaseBaseStrategy
/**
 * Created by gerardin on 10/27/14.
 */
class TestCase3 extends TestCaseBaseStrategy {

    public TestCase3(TestCaseExecutor executor) {
        super(executor)
    }


    @Override
    UserMessage run(String tcid, Map context, String username) {

        context.directTo = "directTo"
        context.directFrom = "directFrom"
        context.wsaTo = context.targetEndpoint
        context.messageType = ArtifactManagement.Type.XDR_FULL_METADATA

        XDRTestStepInterface step = executor.executeSendXDRStep(context)

        //Create a new test record.
        XDRRecordInterface record = new TestCaseBuilder(tcid, username).addStep(step).build()

        executor.db.addNewXdrRecord(record)

        //at this point the test case status is either PASSED or FAILED depending on the result of the validation
        XDRRecordInterface.CriteriaMet testStatus = done(record,step.criteriaMet)

        String report = step.xdrReportItems.last().report

        return new UserMessage(UserMessage.Status.SUCCESS, MsgLabel.XDR_SEND_AND_RECEIVE.msg, new TestCaseEvent(report ,testStatus))
    }
}
