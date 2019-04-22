package com.makerloom.golearn.screens

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.makerloom.common.activity.MyPlainToolbarActivity
import com.makerloom.golearn.R
import com.makerloom.golearn.models.UserInfo
import com.makerloom.golearn.utils.Commons
import com.makerloom.golearn.utils.NetworkUtils
import com.makerloom.golearn.utils.UserUtils
import mehdi.sakout.fancybuttons.FancyButton

class AddUserInfoActivity : MyPlainToolbarActivity(), SubmitActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_info)

        Commons.goToWelcomeIfNotSignedIn(this@AddUserInfoActivity)

        val fNameET: EditText = findViewById(R.id.fname_et)
        val lNameET: EditText = findViewById(R.id.lname_et)

        val user = FirebaseAuth.getInstance().currentUser
        val submitBtn: FancyButton = findViewById(R.id.submit_btn)
        enableBtn(submitBtn)

        submitBtn.setOnClickListener {
            disableBtn(submitBtn)

            if (checkFields(fNameET, lNameET) && user != null) {
                val fName = fNameET.text.toString()
                val lName = lNameET.text.toString()

                UserUtils.submitUserInfo(user, fName, lName, object: UserUtils.UserInfoCallback {
                    override fun onFailure(ex: Exception) {
                        enableBtn(submitBtn)
                        NetworkUtils.showConnectionErrorMessage(this@AddUserInfoActivity, "save your university information")
                    }

                    override fun onSuccess(userInfo: UserInfo?) {
                        // goToAddUniversityInfo()
                        finish()
                        enableBtn(submitBtn)
                    }
                })
            }
        }
    }

    fun checkFields (vararg fields: View): Boolean {
        for (field in fields) {
            if (field is TextView) {
                if (TextUtils.isEmpty(field.text)) {
                    return false
                }
            }
        }
        return true
    }

    fun goToAddUniversityInfo () {
        val intent = Intent(this, AddUniversityInfoActivity::class.java)
        startActivity(intent)
        finish()
    }
}