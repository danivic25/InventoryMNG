package com.example.david.inventorymng.View;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.david.inventorymng.Core.ListViewAdapter;
import com.example.david.inventorymng.Core.Producto;
import com.example.david.inventorymng.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    protected static final int CODIGO_EDITAR_PRODUCTO = 100;
    protected static final int CODIGO_AÑADIR_PRODUCTO = 102;
    protected static final int CODIGO_EST = 104;


    private ArrayList<Producto> items;
    private ArrayAdapter<Producto> adaptadorProducto;
    private ListView lista;
    private InventoryMNG4App app;
    private CreateProduct crear;
    private ListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.app = (InventoryMNG4App) this.getApplication();

        lista = (ListView) this.findViewById( R.id.LvToDoList );

        //Adapatador para la barra de busqueda
        adapter = new ListViewAdapter(this, R.layout.item_listview, app.getItemList());

        // Lista
        adaptadorProducto = new ListViewAdapter(this, R.layout.item_listview, app.getItemList());
        lista.setAdapter(this.adaptadorProducto);

        //lista normal
        lista.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent subActividad = new Intent( MainActivity.this, ModifyProduct.class );

                Producto itemValue = (Producto) lista.getItemAtPosition(position);

                subActividad.putExtra( "pos", itemValue.getCod() );
                MainActivity.this.startActivityForResult( subActividad, CODIGO_EDITAR_PRODUCTO );
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {


                if ( pos >= 0 ) {


                    final AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);

                    final Producto itemValue = (Producto) lista.getItemAtPosition(pos);

                    alerta.setTitle("Eliminar");
                    alerta.setMessage("¿Desea elminar el producto?");
                    alerta.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            app.removeItem( itemValue.getCod() );
                            MainActivity.this.adaptadorProducto.notifyDataSetChanged();
                            MainActivity.this.adapter.notifyDataSetChanged();
                        }
                    });


                    alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });


                    alerta.create().show();
                }
                return false;
            }
        });


        //Boton de añadir
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent( MainActivity.this, CreateProduct.class );

                subActividad.putExtra( "pos", -1 );
                MainActivity.this.startActivityForResult( subActividad, CODIGO_AÑADIR_PRODUCTO );
            }
        });
    }

    //MULTIACTIVIDADES
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CODIGO_AÑADIR_PRODUCTO && resultCode == Activity.RESULT_OK )
        {
            this.adaptadorProducto.notifyDataSetChanged();
            this.adapter.notifyDataSetChanged();
        }


        if (requestCode == CODIGO_EDITAR_PRODUCTO && resultCode == Activity.RESULT_OK )
        {
            this.adaptadorProducto.notifyDataSetChanged();
            this.adapter.notifyDataSetChanged();
        }

        return;
    }

    //MENU DE OPCIONES CON BUSQUEDA
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {

        getMenuInflater().inflate( R.menu.menu_main, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter = new ListViewAdapter(MainActivity.this, R.layout.item_listview, app.getItemList());
                lista.clearTextFilter();
                adapter.filter( newText.toString().trim() );
                lista.invalidate();
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean toret = false;

        switch( item.getItemId() ) {
            case R.id.item_añadirproducto:
                Intent subActividad = new Intent( MainActivity.this, CreateProduct.class );
                subActividad.putExtra( "pos", -1 );
                MainActivity.this.startActivityForResult( subActividad, CODIGO_AÑADIR_PRODUCTO );
                toret = true;
                break;
            case R.id.item_estadisticas:
                Intent subActividad2 = new Intent( MainActivity.this, Plot.class );
                MainActivity.this.startActivityForResult( subActividad2, CODIGO_EST );
                toret = true;
                break;
            case R.id.item_salir:
                System.exit(0);
                toret = true;
                break;
        }

        return toret;
    }

}
