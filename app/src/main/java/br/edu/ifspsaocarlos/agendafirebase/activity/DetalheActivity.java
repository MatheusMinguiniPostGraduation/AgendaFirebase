package br.edu.ifspsaocarlos.agendafirebase.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.edu.ifspsaocarlos.agendafirebase.model.Contato;
import br.edu.ifspsaocarlos.agendafirebase.R;


public class DetalheActivity extends AppCompatActivity {
    private Contato contato;
    private Spinner relacaoContatoSpinner;
    private DatabaseReference databaseReference;
    String FirebaseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        relacaoContatoSpinner = findViewById(R.id.spinnerRelacaoContato);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (getIntent().hasExtra("FirebaseID")) {

            FirebaseID=getIntent().getStringExtra("FirebaseID");


              databaseReference.child(FirebaseID).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    contato = snapshot.getValue(Contato.class);

                    if (contato != null) {
                        EditText nameText = (EditText) findViewById(R.id.editTextNomeContato);
                        nameText.setText(contato.getNome());


                        EditText foneText = (EditText) findViewById(R.id.editTextFoneContato);
                        foneText.setText(contato.getFone());


                        EditText emailText = (EditText) findViewById(R.id.editTextEmailContato);
                        emailText.setText(contato.getEmail());

                        relacaoContatoSpinner.setSelection(Integer.valueOf(contato.getTipoContato()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (FirebaseID==null)
        {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void apagar()
    {

        databaseReference.child(FirebaseID).removeValue();
        Intent resultIntent = new Intent();
        setResult(3,resultIntent);
        finish();
    }

    private void salvar()
    {
        String name = ((EditText) findViewById(R.id.editTextNomeContato)).getText().toString();
        String fone = ((EditText) findViewById(R.id.editTextFoneContato)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextEmailContato)).getText().toString();
        Integer tipoContato = relacaoContatoSpinner.getSelectedItemPosition();

        if (contato==null) {
            contato = new Contato();

            contato.setNome(name);
            contato.setFone(fone);
            contato.setEmail(email);
            contato.setTipoContato(tipoContato.toString());
            databaseReference.push().setValue(contato);

        }
        else
        {
            contato.setNome(name);
            contato.setFone(fone);
            contato.setEmail(email);
            contato.setTipoContato(tipoContato.toString());
            databaseReference.child(FirebaseID).setValue(contato);


        }

        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }
}

