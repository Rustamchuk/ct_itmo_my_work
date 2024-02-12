# rest-api-lesson

# user-service

# REST-API

GET /users -- list of users -- 200, 404, 500

GET /users/:id -- user by id -- 200, 404, 500

POST /users/:id -- create user -- 201, 4**, Header location: url

PUT /users/:id -- fully update users -- 200/204, 404, 400, 500

PATCH /users/:id -- partially update user -- 200/204, 404, 400, 500

DELETE /users/:id -- delete user by id -- 204, 404, 400
