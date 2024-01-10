package myapplication.WorkoutApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.*
import myapplication.WorkoutApp.databinding.ActivityMainBinding
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle
import io.branch.referral.validators.IntegrationValidator
import org.json.JSONException
import java.util.*

class MainActivity : AppCompatActivity() {

    var mDeepLink : String? = null
    var productsList: ArrayList<ProductModel>? = null
    private lateinit var sessionManager: SessionManager

    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sessionManager = SessionManager(this)

//         Check if the user is logged in



        productsList = products.defaultProductList()

        if((sessionManager.isLoggedIn()) && (sessionManager.getProductId()>0))
        {
            route(sessionManager.getProductId().toString())
        }
        val appShareWorkout = BranchUniversalObject()
            .setCanonicalIdentifier("content/12345")
            .setTitle("My Content Title")
            .setContentDescription("My Content Description")
            .setContentImageUrl("https://lorempixel.com/400/400")
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setContentMetadata(ContentMetadata().addCustomMetadata("id", "0").addCustomMetadata("product_id", "0"))

        val lp = LinkProperties()
            .setChannel("messages")
            .setFeature("sharing")
            .setCampaign("7-minute-fitness")

        val ss = ShareSheetStyle(this@MainActivity, "Check this out!", "This stuff is awesome: ")
            .setCopyUrlStyle(ActivityCompat.getDrawable(this, R.drawable.ic_menu_send), "Copy", "Added to clipboard")
            .setMoreOptionStyle(ActivityCompat.getDrawable(this, R.drawable.ic_menu_search), "Show more")
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
            .setAsFullWidthStyle(true)
            .setSharingTitle("Share With")



        binding?.flStart!!.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }
        binding?.flBMI?.setOnClickListener {
            // Launching the BMI Activity
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener {
            // Launching the History Activity
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        binding?.flProducts?.setOnClickListener {

            BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEMS)
                .addCustomDataProperty("user-id", sessionManager.getUserId().toString())
                .logEvent(applicationContext);

            val intent = Intent(this, ProductsList::class.java)
            startActivity(intent)
        }

        binding?.logout?.setOnClickListener {
            Log.e("user-id", sessionManager.getUserId().toString())
            sessionManager.logout()
            Log.e("user-id", sessionManager.getUserId().toString())
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding?.appShareButton?.setOnClickListener {

            appShareWorkout.generateShortUrl(this, lp, Branch.BranchLinkCreateListener {
                    url, error ->
                if (error == null) {
                    mDeepLink = url.toString()
                    Log.i("BRANCH SDK", "got my Branch link to share: " + mDeepLink)
//                    shareLink()
                    appShareWorkout.showShareSheet(this, lp, ss, object : Branch.BranchLinkShareListener {
                        override fun onShareLinkDialogLaunched() {}
                        override fun onShareLinkDialogDismissed() {}
                        override fun onLinkShareResponse(
                            sharedLink: String?,
                            sharedChannel: String?,
                            error: BranchError?
                        ) {
                        }

                        override fun onChannelSelected(channelName: String) {}
                    })
                }
                else
                {
                    Log.i("generated link error", error.toString())
                }
            })


        }

    }
    override fun onStart() {
        super.onStart()
       // Branch.getInstance().disableTracking(true)
//        if (!sessionManager.isLoggedIn()) {
//
//            Log.i("branch params", Branch.getInstance().latestReferringParams.toString())
//            // User is not logged in, redirect to LoginActivity
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            Log.e("user-id", sessionManager.getUserId().toString())
//            finish()
//            // Prevent the user from going back to this activity
//        }
//        else
//        {
//            Log.i("branch params", Branch.getInstance().latestReferringParams.toString())
//            Log.e("user-id", sessionManager.getUserId().toString())
//        }
        val intent = intent
        intent.putExtra("branch_force_new_session", true)

        Branch.sessionBuilder(this).withCallback { branchUniversalObject, linkProperties, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.message)
            } else {
                Log.i("BranchSDK_Tester", "branch init complete!")

                val latestReferringParams = Branch.getInstance().latestReferringParams
                val values = latestReferringParams.getBoolean("+clicked_branch_link")

                Log.e("branchParams", Branch.getInstance().latestReferringParams.toString())
                Log.e("click", values.toString())
                if(!values)
                {
                    Log.e("logOutcall","called logout")
                    callLogout()
                }

//                var status = latestReferringParams["\$clicked_branch_link"]
                var index:String? = latestReferringParams["\$productId"].toString()
//                val status: Boolean
//                try {
//                    Log.e("tried","adbadkjb")
//                    status = latestReferringParams.getBoolean("+clicked_branch_link")
//                    Log.e("status", status.toString())
//                } catch (e: JSONException) {
//                    Log.e("JSON Exception", e.toString())
//                }

                if (branchUniversalObject != null) {
                    Log.i("BranchSDK_Tester", "title " + branchUniversalObject.title)
                    Log.i("BranchSDK_Tester", "CanonicalIdentifier " + branchUniversalObject.canonicalIdentifier)
                    Log.i("BranchSDK_Tester", "metadata " + branchUniversalObject.contentMetadata.convertToJson())
                    val val1 = branchUniversalObject.contentMetadata.customMetadata["id"]
                    val val2 = branchUniversalObject.contentMetadata.customMetadata["product_id"]
                    Log.i("activity id","$val1")
                    Log.i("page_id","$val2")

                    if(val1 == "1")
                    {
                        val product : ProductModel = productsList!![Integer.parseInt(val2!!)-1]
                        val intent = Intent(this@MainActivity, ProductDetail::class.java)
                        intent.putExtra("prod", product )
                        startActivity(intent)

                    }

                }
                if (linkProperties != null) {
                    Log.i("BranchSDK_Tester", "Channel " + linkProperties.channel)
                    Log.i("BranchSDK_Tester", "control params " + linkProperties.controlParams)
                    Log.i("BranchSDK_Tester", "control params reroute" + linkProperties.controlParams["\$productId"].toString())
//                    route(linkProperties.controlParams["\$productId"].toString())
                    if (!sessionManager.isLoggedIn()) {
                        sessionManager.branchData(linkProperties.controlParams["\$productId"]!!.toInt())
                        Log.i("branch params", Branch.getInstance().latestReferringParams.toString())
                        // User is not logged in, redirect to LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Log.e("user-id", sessionManager.getUserId().toString())
                        finish()
                        // Prevent the user from going back to this activity
                    }
                    else
                    {
                        if (index != null) {
                            route(index)
                        }
                    }
                }
//                callLogout()
                Log.e("reach", "reached")
                if(!sessionManager.isLoggedIn())
                {
                    Log.e("insided login","adsdad")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Log.e("user-id", sessionManager.getUserId().toString())
                    finish()
                }
                Log.e("logged-in", "${sessionManager.isLoggedIn()}")
                if (index != null) {
                    Log.e("productid",index)
                }
            }
        }.withData(this.intent.data).reInit()

        IntegrationValidator.validate(this)




    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent!!.putExtra("branch_force_new_session", true)
        val ref = Branch.getInstance().getLatestReferringParams().toString()
        Log.i("branch params new", ref)
        Branch.sessionBuilder(this).withCallback { referringParams, error ->
            if (error != null) {
                Log.e("BranchSDK_TesterNewAbc", error.message)
                Log.e("BranchSDK_TesterNew", referringParams.toString())
                Log.i("BranchSDK_TesterNew", "ref params reroute" + referringParams?.get("productId")
                    .toString())
            } else if (referringParams != null) {
                Log.i("BranchSDK_TesterNew-I", referringParams.toString())
                Log.i("BranchSDK_TesterNew", "reffering params params reroute" + referringParams["\$productId"].toString())
                Log.i("autoInstance", Branch.getInstance().getLatestReferringParams().toString() )

//                route(referringParams["\$productId"].toString())
                if (!sessionManager.isLoggedIn()) {
                    var productid : Int = referringParams["\$productId"].toString().toInt()
                    sessionManager.branchData(productid)
                    Log.i("branch params", Branch.getInstance().latestReferringParams.toString())
                    // User is not logged in, redirect to LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Log.e("user-id", sessionManager.getUserId().toString())
                    finish()
                    // Prevent the user from going back to this activity
                }else
                {

                    route(referringParams["\$productId"].toString())
                }
            }
        }.withData(this.intent.data).reInit()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun callLogout()
    {
        if(!sessionManager.isLoggedIn())
        {
            Log.e("insided login","adsdad")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.e("user-id", sessionManager.getUserId().toString())
            finish()

        }
        Log.e("fun", sessionManager.isLoggedIn().toString())
    }

    fun route(id:String){
        sessionManager.branchData(0)
        val product : ProductModel = productsList!![Integer.parseInt(id!!)-1]
        val intent = Intent(this@MainActivity, ProductDetail::class.java)
        intent.putExtra("prod", product )
        intent.putExtra("branch_force_new_session", true)
        startActivity(intent)
    }
}