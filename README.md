# 📚 Module Management System

**An intuitive Android application for managing educational modules and learning resources.**

---

## 📝 Description

This application enables users to:

- ✅ Create an account and securely log in
- 📘 Browse available educational modules
- ⬇️ Download and manage learning materials
- 📊 Track learning progress
- 🔒 Secure authentication system
- 📱 Modern Material Design interface

---

## 🚀 Features

### 🔐 Authentication

- Email-based registration and login
- Secure session management
- Input validation and user feedback
- Password security requirements

### 📚 Module Management

- View all available modules
- Access detailed module information
- Download content for offline use
- Track personal progress
- Organize learning materials

### 🎨 User Interface

- Modern Material Design components
- Intuitive navigation
- Responsive layout
- User-friendly forms
- Progress indicators

---

## 📱 Requirements

- Android 5.0 (API level 21) or higher
- Internet connection (for downloads)
- Available storage space for files

---

## 📦 Installation

1. 📥 Download the APK from the [Releases](#) section
2. 📲 Install the application on your Android device
3. 🚀 Launch the app and start exploring modules

---

## 🔑 Demo Account

To quickly test the application, you can use the default admin account:

- **Email**: `admin@example.com`
- **Password**: `admin123`

---

## 🛠️ Technical Stack

- **Language**: Java
- **Database**: Room Database
- **UI Framework**: Material Design
- **Architecture**: MVVM
- **Background Processing**: ExecutorService
- **Data Persistence**: SharedPreferences

---

## 🗂️ Project Structure

```
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
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   └── test/
└── build.gradle
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

- **Souhail Ka** - *Initial work* - [souhailka03](https://github.com/souhailka03)

---

## 🙏 Acknowledgments

- Material Design Components
- Android Room Database
- AndroidX Libraries
