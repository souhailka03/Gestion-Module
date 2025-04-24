# 📚 Gestion des Modules

**Une application Android intuitive pour la gestion des modules d'enseignement.**

---

## 📝 Description

Cette application permet aux utilisateurs de :

- ✅ Créer un compte et se connecter
- 📘 Consulter la liste des modules disponibles
- ⬇️ Télécharger et gérer leurs modules
- 📊 Suivre leur progression d'apprentissage

---

## 🚀 Fonctionnalités

### 🔐 Authentification

- Création de compte via email et mot de passe
- Connexion sécurisée avec gestion de session
- Validation des champs et retours utilisateurs

### 📚 Gestion des Modules

- Affichage de tous les modules disponibles
- Accès aux détails de chaque module
- Téléchargement de contenu pour une utilisation hors ligne
- Suivi de progression personnel

### 🎨 Interface Utilisateur

- UI moderne basée sur **Material Design**
- Navigation fluide et intuitive
- Prise en charge du multilingue : 🇫🇷 Français / 🇬🇧 Anglais

---

## 📱 Configuration Requise

- Android **5.0 (API level 21)** ou supérieur
- Connexion Internet (pour le téléchargement)
- Espace de stockage disponible pour les fichiers

---

## 📦 Installation

1. 📥 Téléchargez l'APK depuis la section [Releases](#)
2. 📲 Installez l'application sur votre appareil Android
3. 🚀 Lancez l'application et commencez à explorer les modules

---

## 🔑 Compte Démo

Pour tester rapidement l'application, vous pouvez utiliser ce compte administrateur par défaut :

- **Email** : `admin@example.com`
- **Mot de passe** : `admin123`

---

## 🗂️ Structure du Projet

```bash
app/
├── src/
│   ├── main/
│   │   ├── java/com/ests/gestionmodules/
│   │   │   ├── data/
│   │   │   │   ├── dao/
│   │   │   │   ├── entity/
│   │   │   │   └── AppDatabase.java
│   │   │   ├── LoginActivity.java
│   │   │   ├── RegisterActivity.java
│   │   │   └── ModuleListActivity.java
│   │   └── res/
│   │       ├── layout/
│   │       ├── values/
│   │       └── values-en/
└── build.gradle
```

---

## 🛠️ Technologies Utilisées

- **Android SDK**
- **Room Database**
- **Material Design Components**
- **AndroidX**
- **Kotlin Coroutines**

---

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour participer :

1. 🍴 Forkez le projet
2. 🌿 Créez une nouvelle branche (`feature/ma-fonctionnalité`)
3. 💾 Commitez vos modifications
4. 📤 Poussez votre branche
5. 🛠️ Ouvrez une **Pull Request**

---

## 📄 Licence

Ce projet est sous licence **MIT**. Consultez le fichier [`LICENSE`](./LICENSE) pour plus de détails.
