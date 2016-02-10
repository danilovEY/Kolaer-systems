# Client Uniform System Kolaer

Client Uniform System Kolaer - Module system that is used in the organization KolAER.

## About of client
Client use all employees in KolAER organization. Located .jar file of client on file server, thus one copy used by all employees. In this many advantages, but have one problem - update this client so problematic. Therefore modular architecture has been designed.

## Architecture (v0.0.1 and higher)

Architecture designed in such a way that reading modules on folder and have link on class loader modules. If you need to update the module, close it class loader and delete his dependencies. Why i'm not use OSGi framework? Because the familiarization would take more time than it (development speed was the priority). In future i plan to user OSGi framework.

![](https://3.downloader.disk.yandex.ru/disk/6dd931700cdda2c6c67585377937a06647f9435cadf4cbedde80e948eeaa8754/56bb362e/2-rDbfONtm0r_bYSPb9VuqUBiGitzkt9bN_-5ohd72PhGu5AsTluMB4v80lCyAolh0MZ6itGKsbTnoDITxXJ9A%3D%3D?uid=126133415&filename=Client.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&fsize=65200&hid=3ded4bff5dd474dfb3eff5d4b75b4d7c&media_type=image&tknv=v2&etag=aae579d91482b684dd3401f2adda9219)
