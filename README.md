# takeoff-takehome

A Takeoff Takehome project, to help prepare for our trip to Kyiv in
February 2020.

## Usage

The assumption is that you already have Postgres installed (either
locally or somewhere else). Currently database configuration is inside
db.clj file, so change it to point to your Postgres instance. If
you're using your own database, then you would also need to update the
same file with your Postgres configuration. Or you can create a new
database with the same config options as are in the db.clj file. The
following commands might help you if you're creating your own
database:

pg_ctl -D /usr/local/var/postgres start
createdb takeoff_takehome 
psql -d takeoff_takehome

and then you may want to run the following sql files to create db,
create tables, and insert some initial values:

psql -U takeoff -d takeoff_takehome -f dev/init_db.sql
psql -U takeoff -d takeoff_takehome -f dev/create_tables.sql
psql -U takeoff -d takeoff_takehome -f dev/insert.sql

When database is setup, you can run the following command in the project's root directory:

lein run

that should build code and start a local Web server, listening on
http:/127.0.0.1:8080/.


At this point you can try issuing the following requests:


curl -H "Content-Type: application/json" --data '{"email":"user1@mail.com","password":"password1"}' "http://127.0.0.1:8080/api/generate-token"

with the response:

{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoidXNlcjFAbWFpbC5jb20iLCJleHAiOjE1ODE0NjAwMzl9.Lttu2fJL7YRVowuCd7antouns23tHdWya2POmXaqvVA"}

the response contains a newly generated JWT token that can
subsequently be used to communicate with the service. This token
expires in 1 hour. While valid, the token can be used to get
permissions for that user:

curl -H "X-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoidXNlcjFAbWFpbC5jb20iLCJleHAiOjE1ODE0NjAwMzl9.Lttu2fJL7YRVowuCd7antouns23tHdWya2POmXaqvVA" "http://127.0.0.1:8080/api/permissions"
["permission1_1_1"]

If token expires or an invalid token is used, then the previous request would result in error:

curl -H "X-Token: bad-token" "http://127.0.0.1:8080/api/permissions" 
{"message":"Unauthorized"}

If invalid user/password is supplied on the original request to obtain
a new token, an error is returned:

curl -H "Content-Type: application/json" --data '{"email":"user1@mail.com","password":"BAD PASSWORD"}' "http://127.0.0.1:8080/api/generate-token"
{"message":"Wrong input data"}


Currently users/roles/permissions are setup in a certain way, to make
it easier to see that things work. User1 has 1 permission, user2 has 4
permissions, and user3 has 9 permissions. We saw user1 above.  Here
are examples for users #2 and #3:

curl -H "Content-Type: application/json" --data '{"email":"user2@mail.com","password":"password2"}' "http://127.0.0.1:8080/api/generate-token"
{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoidXNlcjJAbWFpbC5jb20iLCJleHAiOjE1ODE0NjAzNTJ9.hfY4Z2tiq2GD-2YXclsImAoiXXgMyaiWxt11dIi_4fo"}

curl -H "X-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoidXNlcjJAbWFpbC5jb20iLCJleHAiOjE1ODE0NjAzNTJ9.hfY4Z2tiq2GD-2YXclsImAoiXXgMyaiWxt11dIi_4fo" "http://127.0.0.1:8080/api/permissions"
["permission2_1_1","permission2_1_2","permission2_2_1","permission2_2_2"]


curl -H "Content-Type: application/json" --data '{"email":"user3@mail.com","password":"password3"}' "http://127.0.0.1:8080/api/generate-token"
{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoidXNlcjNAbWFpbC5jb20iLCJleHAiOjE1ODE0NjA0Mjl9.-jp5Q6KIgbJNPGVl2gnKQDciwRyt4IKE5VMWRyXCk7M"}


curl -H "X-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoidXNlcjNAbWFpbC5jb20iLCJleHAiOjE1ODE0NjA0Mjl9.-jp5Q6KIgbJNPGVl2gnKQDciwRyt4IKE5VMWRyXCk7M" "http://127.0.0.1:8080/api/permissions"
["permission3_1_1","permission3_1_2","permission3_1_3","permission3_2_1","permission3_2_2","permission3_2_3","permission3_3_1","permission3_3_2","permission3_3_3"]


## Open Issues

There are a number of open issues:

- password is stored in the clear in the database, which is BAD, BAD,
BAD

- there are no unit tests, or any other types of tests.

- SQL statements are directly embedded in Clojure code, should use
jeesql to separarte them out.

- configuration (e.g. for Postgres) is based into Clojure source code,
should use configuration management library, e.g. cprop.

- there isn't any validation on incoming query/body parameters.

- there's no logging

- should probably add containerization, e.g. Docker.


## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
