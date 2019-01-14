mavenCentral()
implementation 'com.facebook.android:facebook-android-sdk:4.28.0'
-----------------------------------------------------------------------------------
In Menifest:-

<meta-data android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id"/>
-------------------------------------------------------------------------------------
XML:-
// custom design button
 <FrameLayout
            android:layout_width="@dimen/social_button_width"
            android:layout_height="wrap_content"
            app:layout_constraintStart_toStartOf="@+id/login_guideline"
            app:layout_constraintTop_toBottomOf="@+id/fL_google"
            android:layout_marginTop="8dp" app:layout_constraintEnd_toStartOf="@+id/login_guideline4"
    >

        <com.facebook.login.widget.LoginButton
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/fb_signIn"
                android:visibility="gone"/>

        <LinearLayout
                android:id="@+id/ll_facebook"
                android:layout_width="match_parent"
                android:layout_height="40dp"
                android:background="@drawable/facebook_button"
                android:orientation="horizontal"
                android:clickable="true"
                android:focusable="true"
                android:foreground="?attr/selectableItemBackground"
        />

    </FrameLayout>
-----------------------------------------------------------------------------------------
fun generateHash(){

 try {
            PackageInfo info = getPackageManager().getPackageInfo("com.duke.myapplication", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("aaaaaaaaaaaaaaaaaaa", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
}
-------------------------------------------------------------------------------------		
// global declare
var callbackManager: CallbackManager? = null

//
onCreate(){

callbackManager = CallbackManager.Factory.create()

ll_facebook.setOnClickListener { fb_signIn.performClick() }

        fb_signIn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val mAccessToken = loginResult.accessToken
                getFbUserProfile(mAccessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }
        })

}
------------------------------------------------------------------------------------
 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        
    }
--------------------------------------------------------------------------------------
 fun getFbUserProfile(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(currentAccessToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(json_object: JSONObject, response: GraphResponse) {
                    try {
                        val responseFB = JSONObject(json_object.toString())
                        //val tempPicture = json_object.getJSONObject("picture").getJSONObject("data").getString("url")
                        val tempName = responseFB.getString("name")
                        val tempEmail = try {
                            responseFB.getString("email")
                        } catch (e: JSONException) {
                            ""
                        }
                        val tempId = responseFB.getString("id")

                        // this for fb logout
                        LoginManager.getInstance().logOut()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,picture.width(200)")
        request.parameters = parameters
        request.executeAsync()
    }