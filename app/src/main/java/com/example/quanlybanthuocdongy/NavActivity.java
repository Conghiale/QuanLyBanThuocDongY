package com.example.quanlybanthuocdongy;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlybanthuocdongy.ui.ChangeMyProfileFragment;
import com.example.quanlybanthuocdongy.ui.ChangePasswordFragment;
import com.example.quanlybanthuocdongy.ui.CreateNewMedicineFragment;
import com.example.quanlybanthuocdongy.ui.HistoryFragment;
import com.example.quanlybanthuocdongy.ui.HomeFragment;
import com.example.quanlybanthuocdongy.ui.MyProfileFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlybanthuocdongy.databinding.ActivityNavBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavBinding binding;
    private TextView tvNavHeaderTitle, tvNavHeaderSubtitle;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MenuItem itemPrint = null;

    public static FirebaseAuth mAuth;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRefAccount = database.getReference("accounts");

    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_CREATE = 1;
    public static final int FRAGMENT_UPDATE = 2;
    public static final int FRAGMENT_MY_PROFILE = 3;
    public static final int FRAGMENT_CHANGE_MY_PROFILE = 4;
    public static final int FRAGMENT_CHANGE_MY_PASSWORD = 5;
    public static final int FRAGMENT_LOGOUT = 6;
    public static int FRAGMENT_CURRENT = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityNavBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance ();
        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        Toolbar toolbar = binding.appBarNav.toolbar;
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener (toggle);
        toggle.syncState ();

        navigationView.setNavigationItemSelectedListener (this);
        replaceFragment (new HomeFragment ());
        navigationView.getMenu ().findItem (R.id.nav_home).setChecked (true);

//        Dexter.withActivity (this)
//                .withPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener (new PermissionListener () {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        itemPrint.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem menuItem) {
//                                createPDFFile(Common.getAppPath(NavActivity.this) + "test_pdf.pdf");
//                                return true;
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                })
//                .check ();
    }

    private void createPDFFile(String path) {
        if (new File (path).exists ())
            new File (path).delete ();
        try {
            Document document = new Document ();
            PdfWriter.getInstance (document, new FileOutputStream (path));
            document.open ();
            document.setPageSize (PageSize.A4);
            document.addCreationDate ();
            document.addAuthor ("Medicine");
            document.addCreator ("Cong Nghia");

            BaseColor colorAccent = new BaseColor (0, 153, 204, 255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            BaseFont fontName = BaseFont.createFont ("assets/fonts/BrandonText-Medium.otf", "UTF-8", BaseFont.EMBEDDED);

            Font titleFont = new Font (fontName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleFont);

            Font orderNumberFont = new Font(fontName, fontSize, Font.NORMAL, colorAccent);
            addNewItem (document, "Order No:", Element.ALIGN_LEFT, orderNumberFont);

            Font orderNumberValueFont = new Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem (document, "#717171", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);

            addNewItem (document, "Oder Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem (document, "5/12/2022", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator (document);

            addNewItem (document, "Account Name: ", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem (document, "Cong Nghia", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator (document);

//            add Product order detail
            addLineSpace (document);
            addNewItem (document, "Product Detail", Element.ALIGN_CENTER, titleFont);
            addLineSeperator (document);

//            item 1
            addNewItemWithLeftAndRight(document, "Hoat Huyet Duong Nao", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "48.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeperator (document);

            addNewItemWithLeftAndRight(document, "Ha Thu O", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "56.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeperator (document);

            addLineSpace (document);
            addLineSpace (document);


            addNewItemWithLeftAndRight(document, "Total", "104000.0", titleFont, orderNumberValueFont);

            document.close ();

            Toast.makeText (this, "Success", Toast.LENGTH_SHORT).show ();
            printPDF();
        } catch (DocumentException e) {
            e.printStackTrace ();
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService (Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(NavActivity.this, Common.getAppPath (NavActivity.this) + "test_pdf.pdf");
            printManager.print ("Document", printDocumentAdapter, new PrintAttributes.Builder ().build ());
        }catch (Exception e){
            Log.e ("TAG", "printPDF: " + e.getMessage ());
        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk (textLeft, textLeftFont);
        Chunk chuckTextRight = new Chunk (textRight, textRightFont);
        Paragraph p = new Paragraph (chunkTextLeft);
        p.add (new Chunk (new VerticalPositionMark ()));
        p.add (chuckTextRight);
        document.add (p);
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator ();
        lineSeparator.setLineColor (new BaseColor (0, 0,0 ,68));
        addLineSpace(document);
        document.add (new Chunk (lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add (new Paragraph (""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk (text, font);
        Paragraph paragraph = new Paragraph (chunk);
        paragraph.setAlignment(align);
        document.add (paragraph);
    }


    @Override
    protected void onStart() {
        super.onStart ();
        View header = navigationView.getHeaderView(0);
        tvNavHeaderTitle = header.findViewById (R.id.tvNavHeaderTitle);
        tvNavHeaderSubtitle = header.findViewById (R.id.tvNavHeaderSubtitle);
        FirebaseUser mCurrentUser = mAuth.getCurrentUser ();

        if(mCurrentUser != null){
            mAuth.addAuthStateListener (firebaseAuth -> {
                myRefAccount.addValueEventListener (new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = snapshot.child (mCurrentUser.getUid ()).child ("username").getValue ().toString ();
                        String email = mCurrentUser.getEmail ();

                        tvNavHeaderTitle.setText (username);
                        tvNavHeaderSubtitle.setText (email);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            });
        }else
            startActivity (new Intent (this, LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        itemPrint = menu.findItem (R.id.action_print);
//        Dexter.withActivity (this)
//                .withPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener (new PermissionListener () {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        itemPrint.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem menuItem) {
//                                createPDFFile(Common.getAppPath(NavActivity.this) + "test_pdf.pdf");
//                                return true;
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                })
//                .check ();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()){
            case R.id.action_print:
                Dexter.withActivity (this)
                        .withPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener (new PermissionListener () {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                createPDFFile(Common.getAppPath(NavActivity.this) + "test_pdf.pdf");
//                                itemPrint.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
//                                    @Override
//                                    public boolean onMenuItemClick(MenuItem menuItem) {
//                                        createPDFFile(Common.getAppPath(NavActivity.this) + "test_pdf.pdf");
//                                        return true;
//                                    }
//                                });
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        })
                        .check ();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen (GravityCompat.START))
            binding.drawerLayout.closeDrawer (GravityCompat.START);
        else
            super.onBackPressed ();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.nav_home){
            if (FRAGMENT_CURRENT != FRAGMENT_HOME) {
                replaceFragment (new HomeFragment ());
                FRAGMENT_CURRENT = FRAGMENT_HOME;
            }
        }
        if (id == R.id.nav_create_new_medicine){
            if (FRAGMENT_CURRENT != FRAGMENT_CREATE){
                replaceFragment (new CreateNewMedicineFragment ());
                FRAGMENT_CURRENT = FRAGMENT_CREATE;
            }
        }
        if (id == R.id.nav_update_inf_medicine){
            if (FRAGMENT_CURRENT != FRAGMENT_UPDATE){
                replaceFragment (new HistoryFragment ());
                FRAGMENT_CURRENT = FRAGMENT_UPDATE;
            }
        }
        if (id == R.id.nav_myProfile){
            if (FRAGMENT_CURRENT != FRAGMENT_MY_PROFILE){
                replaceFragment (new MyProfileFragment ());
                FRAGMENT_CURRENT = FRAGMENT_MY_PROFILE;
//                navigationView.getMenu ().findItem (R.id.nav_myProfile).setChecked (true);
            }
        }
        if (id == R.id.nav_changeMyProfile){
            if (FRAGMENT_CURRENT != FRAGMENT_CHANGE_MY_PROFILE){
                replaceFragment (new ChangeMyProfileFragment ());
                FRAGMENT_CURRENT = FRAGMENT_CHANGE_MY_PROFILE;
//                navigationView.getMenu ().findItem (R.id.nav_changeMyProfile).setChecked (true);
            }
        }
        if (id == R.id.nav_changePassword){
            if (FRAGMENT_CURRENT != FRAGMENT_CHANGE_MY_PASSWORD){
                replaceFragment (new ChangePasswordFragment ());
                FRAGMENT_CURRENT = FRAGMENT_CHANGE_MY_PASSWORD;
//                navigationView.getMenu ().findItem (R.id.nav_changePassword).setChecked (true);
            }
        }
        if (id == R.id.nav_logout){
            if (FRAGMENT_CURRENT != FRAGMENT_LOGOUT){
                mAuth.signOut ();
                Toast.makeText (this, "Successfully Logout", Toast.LENGTH_SHORT).show ();
                Intent intent = new Intent (this, NavActivity.class);
                intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity (intent);
            }
        }
        drawer.closeDrawer (GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
        transaction.replace (R.id.fragment_content, fragment);
        transaction.setReorderingAllowed(true);
        transaction.commit ();
    }
}