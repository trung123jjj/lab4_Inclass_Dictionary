package com.example.lab4_inclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "words";
    public static final String COL_WORD = "word";
    public static final String COL_DEFINITION = "definition";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COL_WORD + " TEXT PRIMARY KEY, "
            + COL_DEFINITION + " TEXT NOT NULL)";

    public DictionaryOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        String[][] words = {
                {"apple", "A round fruit with red or green skin and a whitish interior"},
                {"banana", "A long curved fruit with a yellow skin and soft sweet flesh"},
                {"cat", "A small domesticated carnivorous mammal with soft fur"},
                {"dog", "A domesticated carnivorous mammal with a long snout and acute sense of smell"},
                {"elephant", "A very large plant-eating mammal with a trunk and tusks"},
                {"fish", "A limbless cold-blooded vertebrate animal with gills and fins"},
                {"grape", "A berry, typically green or red, growing in clusters on a vine"},
                {"house", "A building for human habitation"},
                {"internet", "A global computer network providing information and communication facilities"},
                {"java", "A high-level programming language used for software development"},
                {"keyboard", "A panel of keys used for operating a computer or typewriter"},
                {"lemon", "A yellow citrus fruit with acidic juice"},
                {"mountain", "A large natural elevation of the earth's surface"},
                {"notebook", "A book of blank pages used for writing notes"},
                {"orange", "A round juicy citrus fruit with a tough bright reddish-yellow rind"},
                {"python", "A large heavy-bodied nonvenomous snake; also a programming language"},
                {"queen", "A female sovereign ruler of an independent state"},
                {"rainbow", "An arc of colors formed by refraction of sunlight in raindrops"},
                {"sunflower", "A tall plant with a large yellow flower and edible seeds"},
                {"tree", "A woody perennial plant with a single trunk and branches"},
                {"umbrella", "A device providing protection from rain or sun, with a collapsible canopy"},
                {"violet", "A plant with small purple flowers; a bluish-purple color"},
                {"water", "A transparent liquid essential for life, H2O"},
                {"xylophone", "A musical instrument with wooden bars struck with mallets"},
                {"yellow", "A bright primary color between green and orange"},
                {"zebra", "An African wild horse with black-and-white stripes"},
                {"algorithm", "A step-by-step procedure for solving a problem"},
                {"database", "An organized collection of structured data"},
                {"function", "A relation that assigns exactly one output to each input"},
                {"gravity", "The force that attracts objects toward the center of the earth"}
        };

        for (String[] entry : words) {
            ContentValues values = new ContentValues();
            values.put(COL_WORD, entry[0]);
            values.put(COL_DEFINITION, entry[1]);
            db.insert(TABLE_NAME, null, values);
        }
    }
}
