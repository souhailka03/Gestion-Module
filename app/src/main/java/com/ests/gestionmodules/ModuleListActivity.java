package com.ests.gestionmodules;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ests.gestionmodules.adapter.ModuleAdapter;
import com.ests.gestionmodules.data.AppDatabase;
import com.ests.gestionmodules.data.entity.Module;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModuleListActivity extends AppCompatActivity implements ModuleAdapter.OnModuleClickListener {
    private AppDatabase db;
    private ExecutorService executorService;
    private static final String PREFS_NAME = "GestionModulesPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private DownloadManager downloadManager;
    private BroadcastReceiver downloadReceiver;

    public static class DownloadHandler {
        private static AlertDialog downloadDialog;
        private static long downloadId;
        private static ProgressBar progressBar;
        private static TextView progressText;

        public static void showDownloadDialog(Context context, String fileName) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_download, null);
            progressBar = dialogView.findViewById(R.id.downloadProgress);
            progressText = dialogView.findViewById(R.id.downloadProgressText);
            TextView title = dialogView.findViewById(R.id.downloadTitle);
            title.setText("Téléchargement de " + fileName);

            downloadDialog = new MaterialAlertDialogBuilder(context)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();
            downloadDialog.show();
        }

        public static void dismissDialog() {
            if (downloadDialog != null && downloadDialog.isShowing()) {
                downloadDialog.dismiss();
            }
        }

        public static void updateProgress(int progress) {
            if (progressBar != null && progressText != null) {
                progressBar.setProgress(progress);
                progressText.setText(progress + "%");
            }
        }

        public static long getDownloadId() {
            return downloadId;
        }

        public static void setDownloadId(long id) {
            downloadId = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        // Vérifier si l'utilisateur est connecté
        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Configurer l'action bar
        setSupportActionBar(findViewById(R.id.toolbar));

        db = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        
        setupRecyclerView();
        setupFab();
        loadModules();
        insertDefaultModules();
        setupDownloadReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    private void logout() {
        // Effacer les préférences de session
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();

        // Rediriger vers la page de login
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.modulesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ModuleAdapter adapter = new ModuleAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.addModuleFab);
        fab.setOnClickListener(v -> showAddModuleDialog());
    }

    private void loadModules() {
        LiveData<List<Module>> modules = db.moduleDao().getAllModules();
        modules.observe(this, modulesList -> {
            RecyclerView recyclerView = findViewById(R.id.modulesRecyclerView);
            ModuleAdapter adapter = (ModuleAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.setModules(modulesList);
            }
        });
    }

    private void showAddModuleDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_module, null);
        TextInputEditText titleInput = dialogView.findViewById(R.id.titleInput);
        TextInputEditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        TextInputEditText urlInput = dialogView.findViewById(R.id.urlInput);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Ajouter un module")
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String title = String.valueOf(titleInput.getText());
                    String description = String.valueOf(descriptionInput.getText());
                    String url = String.valueOf(urlInput.getText());

                    if (!title.isEmpty() && !description.isEmpty() && !url.isEmpty()) {
                        Module newModule = new Module(title, description, url);
                        executorService.execute(() -> db.moduleDao().insert(newModule));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void showEditModuleDialog(Module module) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_module, null);
        TextInputEditText titleInput = dialogView.findViewById(R.id.titleInput);
        TextInputEditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        TextInputEditText urlInput = dialogView.findViewById(R.id.urlInput);

        titleInput.setText(module.getTitle());
        descriptionInput.setText(module.getDescription());
        urlInput.setText(module.getFileUrl());

        new MaterialAlertDialogBuilder(this)
                .setTitle("Modifier le module")
                .setView(dialogView)
                .setPositiveButton("Modifier", (dialog, which) -> {
                    String title = String.valueOf(titleInput.getText());
                    String description = String.valueOf(descriptionInput.getText());
                    String url = String.valueOf(urlInput.getText());

                    if (!title.isEmpty() && !description.isEmpty() && !url.isEmpty()) {
                        module.setTitle(title);
                        module.setDescription(description);
                        module.setFileUrl(url);
                        executorService.execute(() -> db.moduleDao().update(module));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void onModuleClick(Module module) {
        String fileName = module.getTitle() + ".pdf";
        startDownload(module.getFileUrl(), fileName);
    }

    @Override
    public void onEditClick(Module module) {
        showEditModuleDialog(module);
    }

    @Override
    public void onDeleteClick(Module module) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Supprimer le module")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce module ?")
                .setPositiveButton("Supprimer", (dialog, which) ->
                        executorService.execute(() -> db.moduleDao().delete(module)))
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void setupDownloadReceiver() {
        // Le receiver est maintenant déclaré dans le manifest
        // et géré par le système
    }

    private void startDownload(String fileUrl, String fileName) {
        DownloadManager.Request request = createDownloadRequest(fileUrl, fileName);
        long downloadId = downloadManager.enqueue(request);
        DownloadHandler.setDownloadId(downloadId);
        DownloadHandler.showDownloadDialog(this, fileName);
        startProgressUpdateThread();
    }

    private DownloadManager.Request createDownloadRequest(String fileUrl, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setTitle(fileName)
                .setDescription("Téléchargement en cours...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        return request;
    }

    private void startProgressUpdateThread() {
        new Thread(() -> {
            boolean downloading = true;
            while (downloading) {
                downloading = updateDownloadProgress();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    private boolean updateDownloadProgress() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(DownloadHandler.getDownloadId());
        Cursor cursor = downloadManager.query(query);
        
        try {
            if (cursor.moveToFirst()) {
                int statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (statusColumnIndex >= 0) {
                    int status = cursor.getInt(statusColumnIndex);
                    if (isDownloadComplete(status)) {
                        return false;
                    }
                    updateProgressIfAvailable(cursor);
                }
            }
        } finally {
            cursor.close();
        }
        return true;
    }

    private boolean isDownloadComplete(int status) {
        return status == DownloadManager.STATUS_SUCCESSFUL || 
               status == DownloadManager.STATUS_FAILED;
    }

    private void updateProgressIfAvailable(Cursor cursor) {
        int bytesDownloadedColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
        int bytesTotalColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

        if (bytesDownloadedColumnIndex >= 0 && bytesTotalColumnIndex >= 0) {
            int bytesDownloaded = cursor.getInt(bytesDownloadedColumnIndex);
            int bytesTotal = cursor.getInt(bytesTotalColumnIndex);
            if (bytesTotal > 0) {
                int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                runOnUiThread(() -> DownloadHandler.updateProgress(progress));
            }
        }
    }

    private void insertDefaultModules() {
        executorService.execute(() -> {
            // Vérifier si des modules existent déjà
            List<Module> existingModules = db.moduleDao().getAllModulesSync();
            if (!existingModules.isEmpty()) {
                return; // Ne pas insérer si des modules existent déjà
            }

            List<Module> defaultModules = Arrays.asList(
                new Module("Analyse et visualisation des données Python", 
                          "Découverte de l'analyse de données avec pandas, matplotlib et seaborn.",
                          "https://www.labri.fr/perso/rgiot/cours/anavis/Support/Support.pdf"),
                new Module("Anglais",
                          "Cours d'anglais appliqué à la communication professionnelle et technique.",
                          "https://www.furet.com/media/pdf/feuilletage/9/7/8/2/3/1/1/6/9782311623031.pdf?srsltid=AfmBOor6yPIqXRqexsTnTkbhx9rWQovvUl6ieUHZwqAqNnxGC11pcL9Z"),
                new Module("Base de Données NoSQL MongoDB",
                          "Introduction à MongoDB, requêtes, collections et gestion de documents.",
                          "https://lab-sticc.univ-brest.fr/~ecariou4/cours/si/cours-MongoDB-par6.pdf"),
                new Module("Développement Hybride avec Flutter",
                          "Création d'applications mobiles multi-plateformes avec Flutter.",
                          "https://flutter.dev/docs/get-started/flutter-for/android-devs.pdf"),
                new Module("Entrepreneuriat",
                          "Notions clés pour créer et gérer une entreprise.",
                          "https://e-courseware.cirep.ac.cd/wp-content/uploads/2024/04/UNITE-III-ENTREPRENEURIAT.pdf"),
                new Module("Français",
                          "Maîtrise de la langue française à l'écrit et à l'oral.",
                          "https://eduscol.education.fr/document/16474/download"),
                new Module("IA Générative",
                          "Exploration des modèles d'IA générative comme ChatGPT et DALL·E.",
                          "https://merit.cnrs.fr/wp-content/uploads/2024/01/2024-01-11_IA-GEN-MERIT_Vincent-Guigue.pdf"),
                new Module("Marketing Digital",
                          "Fondamentaux du marketing en ligne et de la stratégie numérique.",
                          "https://144992054.fs1.hubspotusercontent-eu1.net/hubfs/144992054/Le%20grand%20livre%20du%20marketing%20digital.pdf"),
                new Module("Programmation Mobile Android avec Android Studio",
                          "Développement natif Android avec Kotlin et Android Studio.",
                          "https://developer.android.com/training/basics/firstapp/pdf/android-first-app-guide.pdf"),
                new Module("Technologie Back-End avec JAVA et les Web Services",
                          "Création de services REST, SOAP, EJB, JPA, JSF, Spring et Spring Boot.",
                          "https://www.ittcert.fr/ContentV11/doc/pdf/programme/fr/fra/fr/V11.pdf"),
                new Module("Technologie Front-End avec React",
                          "Développement d'interfaces web modernes avec React.",
                          "https://www.eni-elearning.com/wp-content/uploads/fiche-elearning/ET_DEV_REACTJS.pdf"),
                new Module("Méthode Agile",
                          "Gestion de projet avec Scrum, Kanban et méthodes agiles.",
                          "http://projet.eu.org/pedago/sin/ICN/2nde/12-agile.pdf"),
                new Module("UI Design",
                          "Principes de conception d'interfaces utilisateurs intuitives.",
                          "https://profagaskar.files.wordpress.com/2020/03/wiley_the_essential_guide_to_user_interf.pdf"),
                new Module("UX Design",
                          "Création d'expériences utilisateur efficaces et engageantes.",
                          "https://course.ccs.neu.edu/cs5500sp17/09-UX.pdf"),
                new Module("Projet Tutorat 1",
                          "Projet tutoré visant à mettre en pratique les compétences acquises.",
                          "https://github.com/elidrissitv/elidrissitv/blob/main/README.md"),
                new Module("Projet Tutorat 2",
                          "Deuxième phase de projet collaboratif avec soutenance finale.",
                          "https://github.com/elidrissitv/elidrissitv/blob/main/README.md"),
                new Module("Sécurisation des Applications Web et Mobiles",
                          "Bonnes pratiques de cybersécurité pour les applications.",
                          "https://www.irit.fr/~Yannick.Chevalier/Cours/Securite/cours.pdf"),
                new Module("Droit, Civisme et Citoyenneté",
                          "Initiation aux droits, devoirs et principes de citoyenneté numérique.",
                          "https://www.un.org/en/universal-declaration-human-rights/UDHR.pdf"),
                new Module("Développement de Jeux",
                          "Création de jeux simples en 2D avec des moteurs comme Unity ou Flutter.",
                          "https://unity.com/learn/tutorials/pdf/game-development-guide.pdf"),
                new Module("Développement iOS",
                          "Développement d'apps iOS avec Swift et Xcode.",
                          "https://developer.apple.com/documentation/swift/swift-programming-language-guide.pdf"),
                new Module("Développement Hybride avec React Native",
                          "Développement d'applications mobiles avec React Native.",
                          "https://reactnative.dev/docs/getting-started/react-native-beginners-guide.pdf")
            );

            for (Module module : defaultModules) {
                db.moduleDao().insert(module);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }
        executorService.shutdown();
    }

    private void onDownloadComplete(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);

        if (cursor != null && cursor.moveToFirst()) {
            int statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(statusColumnIndex);

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                int uriColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String localUri = cursor.getString(uriColumnIndex);
                Toast.makeText(this, "Téléchargement terminé", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Échec du téléchargement", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
        DownloadHandler.dismissDialog();
    }

    private void updateModuleUserDownloadStatus(int moduleId, int userId, boolean isDownloaded, String localFilePath) {
        // Cette méthode n'est plus utilisée car nous n'utilisons plus moduleUserViewModel
        // Elle peut être supprimée ou modifiée selon les besoins
    }
} 