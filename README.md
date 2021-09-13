# Моя аптечка
Моя аптечка – бакалаврская дипломная работа, приложение для мобильной операционной системы Android, в котором есть следующие функции:
 - хранение информации о лекарствах в виде списка;
 - хранение информации об остатках лекарства, их сроках годности;
 - оповещения об истекающих сроках годности и о лекарствах, которые подходят к концу;
 - хранение данных в интернете с локальным кешированием, получения доступа к данным путем авторизации через электронную почту;
 - добавление лекарств через поиск по названию или по штрих коду;
 - добавления напоминаний о приеме лекарств, гибкая настройка графика и схемы приема;

Написано с применением MVVM (ViewModel + LiveData), DI (Koin), Network part (retrofit2 + coroutines), Navigation (NavigationComponent), Google Material, 
Room, WorkManager (для уведомлений о приеме), Firebase (auth, firestore).
База данных лекарств хранится в веб-сервисе написанном на Ktor.

## Скриншоты
<img src="/screens/screen1.jpeg"> 
<img src="/screens/screen2.jpeg"> 
<img src="/screens/screen3.jpeg"> 
<img src="/screens/screen4.jpeg"> 
<img src="/screens/screen5.jpeg">
<img src="/screens/screen6.jpeg">
<img src="/screens/screen7.jpeg"> 
<img src="/screens/screen8.jpeg"> 
<img src="/screens/screen9.jpeg"> 
