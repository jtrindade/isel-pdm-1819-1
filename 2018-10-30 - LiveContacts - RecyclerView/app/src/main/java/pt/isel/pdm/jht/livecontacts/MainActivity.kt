package pt.isel.pdm.jht.livecontacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val contactsModel by lazy {
        ViewModelProviders
                .of(this)
                .get(ContactsViewModel::class.java)
    }

    private val REQUEST_READ_CONTACTS = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactsView.setHasFixedSize(true)
        contactsView.layoutManager = LinearLayoutManager(this)

        /*
        listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    Toast.makeText(
                            this,
                            (listView.adapter.getItem(position) as Map<String, String>)["name"],
                            Toast.LENGTH_SHORT
                    ).show()
                }
        */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_READ_CONTACTS
            )

        } else {
            continueOnCreate()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                continueOnCreate()
            } else {
                Toast.makeText(this, "-- NO PERMISSION --", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun continueOnCreate() {
        contactsModel.contacts.observe(this, Observer<List<ContactSummary>> {
            contactsView.adapter = ContactsAdapter(it)
        })
    }
}
