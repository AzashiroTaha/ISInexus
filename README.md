# 🎓 ISI Nexus

A Java application for managing and scheduling thesis defense sessions (PFE/Licence) at ISI. The project offers **two modes**: a Swing graphical interface and an interactive command-line interface (CLI).

---

## ✨ Features

- **Full CRUD management** for students, teachers, and rooms
- **Defense session scheduling** with room, date/time, and full jury assignment (President, Examiner, Supervisor)
- **Automatic conflict detection** via MySQL triggers:
  - Room already booked at the same time slot
  - Teacher already assigned to another defense at the same time
  - Duplicate jury members within the same defense
- **Graphical interface** (Swing + Nimbus look & feel) with dynamic JComboBoxes populated from the database
- **Interactive CLI mode** for use without a graphical interface

---

## 🗂️ Project Structure

```
src/com/isi/nexus/
├── Main.java              # Entry point — CLI mode
├── MainUI.java            # Entry point — Swing mode (Nimbus)
│
├── model/                 # Data models
│   ├── Admin.java
│   ├── Etudiant.java
│   ├── Enseignant.java
│   ├── Salle.java         # (maps to: classe table)
│   └── Soutenance.java
│
├── dbpack/                # Data access layer (DAO / JDBC)
│   ├── Admin_db.java
│   ├── Etudiant_db.java
│   ├── Enseignant_db.java
│   ├── Salle_db.java
│   └── Soutenance_db.java
│
├── service/               # Business logic
│   ├── AdminService.java
│   ├── EtudiantService.java
│   ├── EnseignantService.java
│   ├── SalleService.java
│   └── SoutenanceService.java
│
├── uipack/                # Swing graphical interface
│   ├── MainUIFrame.java
│   ├── EtudiantUI.java
│   ├── EnseignantUI.java
│   ├── SalleUI.java
│   ├── SoutenanceUI.java
│   ├── UITheme.java        # Centralized visual theme (ISI blue palette)
│   └── DataChangeListener.java
│
├── clipack/               # Command-line interface
│   ├── EtudiantCLI.java
│   ├── EnseignantCLI.java
│   ├── SalleCLI.java
│   └── SoutenanceCLI.java
│
└── util/
    ├── DBConnection.java  # JDBC connection (password via env variable)
    └── funcs.java
```

---

## 🗄️ Database

**Database name:** `isinexus_db`  
**Dedicated MySQL user:** `nexus`

| Table | Description |
|-------|-------------|
| `Admins` | Administrator accounts |
| `etudiant` | Students with registration number and thesis topic |
| `enseignant` | Teachers with rank and speciality |
| `classe` | Rooms (unique name, capacity, video projector) |
| `soutenance` | Defense sessions with FK to room, student, and 3 jury teachers |

Conflict enforcement is handled by **two MySQL triggers** (`BEFORE INSERT` and `BEFORE UPDATE`) that automatically block any scheduling conflict.

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 11+
- MySQL 8+

### 1. Clone the repository

```bash
git clone https://github.com/AzashiroTaha/ISInexus.git
cd ISInexus
```

### 2. Set up the database

```bash
mysql -u root -p < db/script.sql
```

### 3. Set the database password

The MySQL password is read from the **`DB_PASS` environment variable**:

```bash
# Linux / macOS
export DB_PASS="YourPassword"

# Windows (PowerShell)
$env:DB_PASS="YourPassword"
```

> Make sure `DB_PASS` matches the password defined in `db/script.sql` for the `nexus` user.

### 4. Run the application

**Graphical interface mode:**
```bash
java -jar out/artifacts/isi_nexus_jar/isi_nexus.jar
```

**CLI mode:**
```bash
java -cp out:library/mysql-connector-j-9.6.0.jar com.isi.nexus.Main
```

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java (JDK 11+) |
| GUI | Java Swing (Nimbus look & feel) |
| Database | MySQL 8 |
| JDBC Driver | mysql-connector-j 9.6.0 |
| IDE | IntelliJ IDEA |

**Note :** AI is only used for debugging not for code completion or replacement!

---

## 👨‍💻 Author
 **Abdoulaye Taha DIOUF**
