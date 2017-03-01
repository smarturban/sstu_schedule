# sstu_schedule
### Platform for synchronize your schedule and Rvuzov service.

* ``Java >= 1.8``
* ``Maven >= 3.6.1``
* ``MongoDB >= 3.2.9``

Contact https://vk.com/rvuzov for access token.

####For deploying of Heroku please use environment variables!

About ``application.yml``:
<br><br>
``university.name`` - full name of university
<br>
``university.addr`` - short name of university
<br>
``university.url`` - url of university server for schedule import 
<br><br>
If your server use basic auth, see ``university.basicAuth.login`` and ``university.basicAuth.password``
<br><br>
``spring.data.mongodb.uri`` - uri for MongoDB
<br><br>
``import.token`` - your token for access to Rvuzov server for synchronize schedule
<br>
``import.report`` - email for reports about synchronization

Thanks <b>smarturban</b> team: [@alekseiko](https://github.com/alekseiko), [@SevakAvet](https://github.com/SevakAvet), [@maxbr](https://github.com/maxbr) and [@vovcyan](https://github.com/vovcyan) for this.
