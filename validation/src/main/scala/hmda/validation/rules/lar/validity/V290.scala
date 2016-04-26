package hmda.validation.rules.lar.validity

import hmda.model.census.CBSATractLookup
import hmda.model.fi.lar.LoanApplicationRegister
import hmda.validation.dsl.{ Failure, Result }
import hmda.validation.rules.EditCheck

object V290 extends EditCheck[LoanApplicationRegister] with CensusEditCheck {

  val cbsaTracts = CBSATractLookup.values

  override def name: String = "V290"

  def failureMessage = "MSA/MD, state, and county codes do not = a valid combination"

  override def apply(input: LoanApplicationRegister): Result = {
    val msa = msaCode(cbsaTracts, input.geography.msa)
    val state = input.geography.state
    val county = state + input.geography.county

    val combination = (state, county, msa)

    val validCombinations = cbsaTracts.map { cbsa =>
      (cbsa.state, cbsa.county, cbsa.geoidMsa)
    }

    when(msa not equalTo("NA")) {
      combination is containedIn(validCombinations)
    }
  }

}
