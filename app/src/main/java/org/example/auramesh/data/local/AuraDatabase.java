package org.example.auramesh.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.example.auramesh.data.models.AuraMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Versiyon numarası önemlidir. Modele yeni bir sütun eklersen version'u artırmalısın.
@Database(entities = {AuraMessage.class}, version = 1, exportSchema = false)
public abstract class AuraDatabase extends RoomDatabase {

    public abstract AuraMessageDao auraMessageDao();

    // Singleton (Tekil) veritabanı örneği
    private static volatile AuraDatabase INSTANCE;

    // Veritabanı işlemlerini arka planda (batarya dostu) yapmak için Thread Havuzu
    private static final int NUMBER_OF_THREADS = 1;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Veritabanını uygulamada tek bir merkezden çağırmak için
    public static AuraDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AuraDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AuraDatabase.class, "auramesh_dtn_database")
                            // Uygulama çökerse veritabanını sıfırla (MVP için güvenli yol)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}