# Trip Planner App ✈️🌍

A mobile application that provides **personalized travel recommendations** for flights, hotels, restaurants, and attractions, with **real-time data fetching** and an **offline-first chat system**.  

Built as a final-year Computer Science project at **Bar-Ilan University** by **Ariel Oscar & Bar Ben Tovim**, focusing on **mobile development, async APIs, and database design**.

---

## 🚀 Features
- 🔐 **User Authentication** with Firebase  
- 🗺 **Personalized recommendations** for flights, hotels, and attractions  
- ⏱ **Real-time data fetching** from multiple APIs  
- 💬 **Chat system with offline storage** using **Room**, synchronizes when reconnected  
- 📊 **Smart travel suggestions** powered by **OpenAI API**  
- 🖌 **Modern UI/UX** designed in **Figma**  
- 💾 **Cloud storage** with **MongoDB Atlas & Firebase**

---

## 🛠 Tech Stack

### Mobile (Frontend)
- Android (Java/XML), Android Studio  
- Room (offline local DB for chat caching)

### Backend
- Node.js, Express.js  
- Firebase

### Database
- MongoDB Atlas  
- Firebase Realtime Database  
- Room (local persistence)

### Other Tools
- Figma (UI/UX design)  
- Git & GitHub (version control)

---

## 💻 Setup & Installation
1. Clone the repository:  
    git clone https://github.com/56bar56/finalProject.git

2. Open in **Android Studio** and build the project.  
3. Ensure the **server repository** is running for full backend functionality, you can clone it's repository here:  https://github.com/56bar56/serverFinalProject.git
4. Run on an emulator or Android device.

---

## 📱 Usage
- Launch the app to start a new trip or view existing trips.  
- Navigate through **flights, hotels, restaurants, and attractions**.  
- Swipe through trip images using **ViewPager2**.  
- Select multiple options; selections are highlighted.  
- Chat with friends offline; messages sync automatically when connected.

---

## 🔑 Challenges & Learning
- Handling **real-time asynchronous API calls** for external travel data  
- Designing a **flexible database schema** for user preferences and bookings  
- Implementing an **offline-first chat system** with Room and synchronization logic  
- Integrating **AI-based recommendations** with OpenAI API  
- Ensuring **smooth mobile performance** while managing multiple dynamic views

---

## 📂 Project Structure
- `app/src/main/java` – Android app source code  
- `app/src/main/res` – XML layouts, drawables, and resources  
- `serverFinalProject` – Backend API for storing/retrieving trip data

---

## 📜 License
Open-source for educational purposes.
