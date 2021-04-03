package com.example.businesscardreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;

import com.example.businesscardreader.ProfileListActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.FileProvider;

import android.Manifest;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import butterknife.BindView;
import butterknife.OnClick;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.businesscardreader.FileCompressor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.example.businesscardreader.ProfileListActivity;
import com.example.businesscardreader.CameraReaderActivity;

public class ProfileCreatorActivity extends AppCompatActivity {

    private final int PICK_PHOTO = 1;

    Bitmap mBitmap;

    Intent intent;
    //scanning buttons
    private static final String TAG = "MainActivity";

    // Image veriables for camera or gallery
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;
    FileCompressor mCompressor;
    @BindView(R.id.imageView)
    ImageView imageView;
    Bitmap image;
    String datapath = "";
    ImageView testImage;
    ImageView img;
    Bitmap contact_bitmap;
    TextView textViewTakeImage;

    TextView editText;

    private TessBaseAPI mTess;


    private Profile profile;

    private EditText nameInput;
    private EditText jobTitleInput;
    private EditText companyInput;
    private EditText telephoneInput;
    private EditText emailInput;
    private Button nameCandidatesButton;
    private Button jobTitleCandidatesButton;
    private Button companyCandidatesButton;
    private Button telephoneCandidatesButton;
    private Button emailCandidatesButton;
    private Button save_contact;


    Map<String, Integer> phoneNumberCandidates = new HashMap<String, Integer>();
    Map<String, Integer> emailCandidates = new HashMap<String, Integer>();
    List<String> genericCandidates = new ArrayList<String>();
    List<String> nameCandidates = new ArrayList<String>();
    List<String> companyCandidates = new ArrayList<String>();

    private ProfileDao profileDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        nameInput = (EditText) findViewById(R.id.input_name);
        jobTitleInput = (EditText) findViewById(R.id.input_job_title);
        companyInput = (EditText) findViewById(R.id.input_company);
        telephoneInput = (EditText) findViewById(R.id.input_telephone);
        emailInput = (EditText) findViewById(R.id.input_email);

        nameCandidatesButton = (Button) findViewById(R.id.name_candidates_button);
        jobTitleCandidatesButton = (Button) findViewById(R.id.job_title_candidates_button);
        companyCandidatesButton = (Button) findViewById(R.id.company_candidates_button);
        telephoneCandidatesButton = (Button) findViewById(R.id.telephone_candidates_button);
        emailCandidatesButton = (Button) findViewById(R.id.email_candidates_button);
        save_contact = (Button) findViewById(R.id.save_contact);


        testImage = (ImageView) findViewById(R.id.imageView);
        //init image
        image = BitmapFactory.decodeResource(getResources(), R.drawable.camera1);

        if (!generateProfile()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.profile_creator_alert_read_fail);
            builder.setNeutralButton(R.string.profile_creator_alert_read_fail_retry,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ProfileCreatorActivity.this,
                                    CameraReaderActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton(R.string.profile_creator_alert_read_fail_manual,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            builder.setPositiveButton(R.string.profile_creator_alert_read_fail_exit,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ProfileCreatorActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.show();
        }

        profileDao = ProfileDao.getInstance(this);

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button rescanButton = (Button) findViewById(R.id.rescan_button);
        Button exitButton = (Button) findViewById(R.id.exit_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                validateAndCreateProfile();
            }
        });
        rescanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmRescan();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmExit();
            }
        });

        nameCandidatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpCandidates(nameCandidates, nameInput);
            }
        });
        jobTitleCandidatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpCandidates(genericCandidates, jobTitleInput);
            }
        });
        companyCandidatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpCandidates(companyCandidates, companyInput);
            }
        });
        telephoneCandidatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpCandidates(phoneNumberCandidates.keySet(), telephoneInput);
            }
        });
        emailCandidatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popUpCandidates(emailCandidates.keySet(), emailInput);
            }
        });
        save_contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addToContacts();
            }
        });

//        //image
//        testImage.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                //selectImage();
//                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(photoCaptureIntent, 7);
//            }
//        });


        //new code
        // Defining OnClick listener for the photo
//        OnClickListener photoClickListener = new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, PICK_PHOTO);
//            }
//        };
//
//        // Defining OnClick listener for the Add Contact Button
//        OnClickListener addClickListener = new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                // Getting reference to Name EditText
//                EditText etName = (EditText) findViewById(R.id.et_name);
//
//                // Getting reference to Mobile EditText
//                EditText etMobile = (EditText) findViewById(R.id.et_mobile);
//
//                ArrayList<ContentProviderOperation> ops =
//                        new ArrayList<ContentProviderOperation>();
//
//                int rawContactID = ops.size();
//
//                // Adding insert operation to operations list
//                // to insert a new raw contact in the table ContactsContract.RawContacts
//                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
//                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
//                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
//                        .build());
//
//                // Adding insert operation to operations list
//                // to insert display name in the table ContactsContract.Data
//                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
//                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
//                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, etName.getText().toString())
//                        .build());
//
//                // Adding insert operation to operations list
//                // to insert Mobile Number in the table ContactsContract.Data
//                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
//                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, etMobile.getText().toString())
//                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
//                        .build());
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                if (mBitmap != null) {    // If an image is selected successfully
//                    mBitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
//
//                    // Adding insert operation to operations list
//                    // to insert Photo in the table ContactsContract.Data
//                    boolean add = ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
//                            .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
//                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
//                            .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
//                            .build());
//
//                    try {
//                        stream.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                try {
//                    // Executing all the insert operations as a single database transaction
//                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//                    Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
//                } catch (RemoteException
//                        e) {
//                    e.printStackTrace();
//                } catch (OperationApplicationException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        // Creating a button click listener for the "Add Contact" button
//        View.OnClickListener contactsClickListener = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Creating an intent to open Android's Contacts List
//                Intent contacts = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
//
//                // Starting the activity
//                startActivity(contacts);
//            }
//        };
//
//        // Getting reference to ImageView
//        ImageButton ibPhoto = (ImageButton) findViewById(R.id.ib_photo);
//
//        // Getting Reference to Add Contact Button
//        Button btnAdd = (Button) findViewById(R.id.btn_add);
//
//        // Getting Reference to Contact List Button
//        Button btnList = (Button) findViewById(R.id.btn_list);
//
//        // Setting OnClick Listener for the photo
//        ibPhoto.setOnClickListener(photoClickListener);
//
//        // Setting OnClick Listener of the Add Contact button
//        btnAdd.setOnClickListener(addClickListener);
//
//        // Setting OnClick Listener for the Contacts List button
//        btnList.setOnClickListener(contactsClickListener);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 7 && resultCode == RESULT_OK) {
//
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//            imageView.setImageBitmap(bitmap);
//        } else {
//            switch (requestCode) {
//                case PICK_PHOTO:
//                    if (resultCode == RESULT_OK) {
//                        // Getting the uri of the picked photo
//                        Uri selectedImage = data.getData();
//
//                        InputStream imageStream = null;
//                        try {
//                            // Getting InputStream of the selected image
//                            imageStream = getContentResolver().openInputStream(selectedImage);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//
//                        // Creating bitmap of the selected image from its inputstream
//                        mBitmap = BitmapFactory.decodeStream(imageStream);
//
//                        // Getting reference to ImageView
//                        ImageButton ibPhoto = (ImageButton) findViewById(R.id.ib_photo);
//
//                        // Setting Bitmap to ImageButton
//                        ibPhoto.setImageBitmap(mBitmap);
//                    }
//            }
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
    /*
Image methods are below
 */

    /**
     * Alert dialog for capture or select from galley
     */
//    private void selectImage() {
//        final CharSequence[] items = {"Take Photo", "Choose from Library",
//                "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileCreatorActivity.this);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (items[i].equals("Take Photo")) {
//                    requestStoragePermission(true);
//                } else if (items[i].equals("Choose from Library")) {
//                    requestStoragePermission(false);
//                } else if (items[i].equals("Cancel")) {
//                    dialogInterface.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileCreatorActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // TODO Auto-generated method stub
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                File f = new File(Environment.getExternalStorageDirectory()
//                        .toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
//                try {
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                            bitmapOptions);
//
//                    contact_bitmap = bitmap;
//
//                    imageView.setImageBitmap(bitmap);
//
//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System
//                            .currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (requestCode == 2) {
//
//                Uri selectedImage = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage, filePath,
//                        null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image .", picturePath + "");
//                contact_bitmap = thumbnail;
//                imageView.setImageBitmap(thumbnail);
//                try {
//                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Glide.with(ProfileCreatorActivity.this)
//                        .load(mPhotoFile)
//                        .apply(new RequestOptions().placeholder(R.drawable.camera1))
//                        .into(imageView);
//            }
//        }
//    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    mPhotoFile = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                }
            }
        }
    }


    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }


    //   @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_TAKE_PHOTO) {
//                try {
//                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Glide.with(ProfileCreatorActivity.this)
//                        .load(mPhotoFile)
//                        .apply(new RequestOptions().placeholder(R.drawable.camera1))
//                        .into(imageView);
//            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
//                Uri selectedImage = data.getData();
//                try {
//                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Glide.with(ProfileCreatorActivity.this)
//                        .load(mPhotoFile)
//                        .apply(
//                                new RequestOptions().placeholder(R.drawable.camera1))
//                        .into(imageView);
//
//            }
//        }
//    }

    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //image
    @OnClick(R.id.imageView)
    public void onViewClicked() {
        selectImage();
    }

    private void addToContacts() {

//        nameInput.getText().toString();
//        jobTitleInput.getText().toString();
//        companyInput.getText().toString();
//        telephoneInput.getText().toString();
//        emailInput.getText().toString();

        // Creates a new Intent to insert a contact
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        //Checks if we have the name, email and phone number...
        if (nameInput.getText().length() > 0 && (telephoneInput.getText().length() > 0 || emailInput.getText().length() > 0)) {
            //Adds the name...
            intent.putExtra(ContactsContract.Intents.Insert.NAME, nameInput.getText().toString());

            //Adds the email...
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, emailInput.getText().toString());
            //Adds the email as Work Email
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);

            //Adds the phone number...
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, telephoneInput.getText());
            //Adds the phone number as Work Phone
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);

            //starting the activity...
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "No information to add to contacts!", Toast.LENGTH_LONG).show();
        }

    }

    private void confirmRescan() {
        dialogConfirm(R.string.profile_creator_confirm_rescan,
                R.string.profile_creator_button_rescan,
                CameraReaderActivity.class);
    }

    private void confirmExit() {
        dialogConfirm(R.string.profile_creator_confirm_exit,
                R.string.profile_creator_button_exit,
                MainActivity.class);
    }

    private void dialogConfirm(int dialogMessage,
                               int confirmMessage,
                               final Class newActivityClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_confirmation);
        builder.setMessage(dialogMessage);
        builder.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.setPositiveButton(confirmMessage,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ProfileCreatorActivity.this,
                                newActivityClass);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.show();
    }

    private void popUpCandidates(Collection<String> candidates, final EditText input) {
        if (!candidates.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final CharSequence[] items = candidates.toArray(new CharSequence[candidates.size()]);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInteface, int i) {
                    input.setText(items[i]);
                }
            });
            builder.show();
        }
    }

    private void validateAndCreateProfile() {

        Profile profile = new Profile(
                nameInput.getText().toString(),
                jobTitleInput.getText().toString(),
                companyInput.getText().toString(),
                telephoneInput.getText().toString(),
                emailInput.getText().toString()
        );
        if (profile.isValid()) {
            if (saveProfile(profile)) {
                showSaveSuccessDialog();
            } else {
                Utils.displayErrorDialog(this);
            }
        } else {
            alertInvalidProfile();
        }
    }

    private boolean saveProfile(Profile profile) {
        return profileDao.insert(profile);
    }

    private void alertInvalidProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.profile_creator_save_invalid_title);
        builder.setMessage(R.string.profile_creator_save_invalid_message);
        builder.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
    }

    private void showSaveSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_success);
        builder.setMessage(R.string.profile_creator_save_success_message);
        builder.setCancelable(false); //Don't let them touch out!
        builder.setNegativeButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ProfileCreatorActivity.this,
                                ProfileListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setPositiveButton(R.string.profile_creator_save_success_scan_another,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ProfileCreatorActivity.this,
                                CameraReaderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.create().show();
    }

    private boolean generateProfile() {
        ArrayList<String> profileData;
        try {
            profileData = getIntent().getStringArrayListExtra(CameraReaderActivity.PROFILE_DATA_KEY);
        } catch (Exception e) {
            Log.w(ProfileCreatorActivity.class.getName(), Log.getStackTraceString(e));
            return false;
        }

        for (String snapshot : profileData) {
            for (String text : snapshot.split("\n")) {
                int selected = 0;
                selected = selectPhoneNumber(text, phoneNumberCandidates)
                        + selectEmail(text, emailCandidates);
                if (selected == 0) {
                    selectRest(text, genericCandidates);
                }
            }
        }
        boolean generateProfile = false;
        String phoneNumber = getBestCandidate(phoneNumberCandidates);
        if (StringUtils.isNotBlank(phoneNumber)) {
            generateProfile = true;
            telephoneInput.setText(phoneNumber);
        }
        String email = getBestCandidate(emailCandidates);

        if (StringUtils.isNotBlank(email)) {
            generateProfile = true;
            emailInput.setText(email);
            String namePart = email.substring(0, email.indexOf("@"));
            String companyPart = email.substring(email.indexOf("@") + 1, email.length());
            companyPart = companyPart.substring(0, companyPart.indexOf("."));

            StringBuilder nameBuilder = new StringBuilder();
            int j = 0;
            for (String str : namePart.split("\\.")) {
                j++;
                nameBuilder.append(str.substring(0, 1).toUpperCase());
                if (str.length() > 1) {
                    nameBuilder.append(str.substring(1));
                }
                nameBuilder.append(" ");
            }
            if (j > 0) {
                nameCandidates.add(nameBuilder.toString().trim());
            }

            if (companyPart.length() > 1
                    && !companyPart.equals("googlemail")
                    && !companyPart.equals("gmail")
                    && !companyPart.equals("hotmail")
                    && !companyPart.equals("live")) {
                companyCandidates.add(companyPart.substring(0, 1).toUpperCase() + companyPart.substring(1));
                companyCandidates.add(companyPart.toUpperCase());
                companyCandidates.add(companyPart);
            }

        }

        nameCandidates.addAll(genericCandidates);
        companyCandidates.addAll(genericCandidates);

        if (!nameCandidates.isEmpty()) {
            nameInput.setText(nameCandidates.get(0));
            generateProfile = true;
        }
        int i = 0;
        if (!companyCandidates.isEmpty()) {
            if (companyCandidates.get(0).equals(nameCandidates.get(0)) && companyCandidates.size() != 1) {
                i++;
            }
            companyInput.setText(companyCandidates.get(i));
        }

        if (!genericCandidates.isEmpty()) {
            jobTitleInput.setText(genericCandidates.get(0));
        }

        genericCandidates.addAll(phoneNumberCandidates.keySet());
        genericCandidates.addAll(emailCandidates.keySet());
        return generateProfile;
    }

    private void selectRest(String text, List<String> genericCandidates) {
        List<String> toFilter = new ArrayList<String>();
        boolean filter = false;
        for (String candidate : genericCandidates) {
            if (candidate.contains(text)) {
                filter = true;
                break;
            }
            if (text.contains(candidate)) {
                toFilter.add(candidate);
            }
        }
        if (!filter) {
            genericCandidates.add(text);
        }
        genericCandidates.removeAll(toFilter);
    }

    private int selectPhoneNumber(String text, Map<String, Integer> phoneNumberCandidates) {
        //At least 6 numbers, allow other characters
        String trimmed = text.toLowerCase().replaceAll("tel:", "").replaceAll("mob:", "").trim();
        if (phoneNumberCandidates.containsKey(trimmed)) {
            phoneNumberCandidates.put(trimmed, phoneNumberCandidates.get(trimmed) + 1);
        } else {
            int numCount = 0;

            for (char c : trimmed.toCharArray()) {
                if (Character.isDigit(c)) {
                    numCount++;
                }
                if (numCount == 6) {
                    phoneNumberCandidates.put(trimmed, 1);
                    return 1;
                }
            }
        }
        return 0;
    }

    private int selectEmail(String text, Map<String, Integer> emailCandidates) {
        int atPos = text.indexOf("@");
        int dotPos = text.lastIndexOf(".");
        //Very basic check to see if a text COULD BE an email address
        if (atPos != -1 && dotPos > atPos) {
            String trimmed = text.trim();
            if (emailCandidates.containsKey(trimmed)) {
                emailCandidates.put(trimmed, emailCandidates.get(trimmed) + 1);
            } else {
                emailCandidates.put(trimmed, 1);
            }
            return 1;
        }
        return 0;
    }

    private String getBestCandidate(Map<String, Integer> candidates) {
        int maxValue = 0;
        String bestCandidate = "";
        for (Map.Entry<String, Integer> candidate : candidates.entrySet()) {
            if (candidate.getValue() > maxValue) {
                maxValue = candidate.getValue();
                bestCandidate = candidate.getKey();
            }
        }
        //candidates.remove(bestCandidate);
        return bestCandidate;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileCreatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}