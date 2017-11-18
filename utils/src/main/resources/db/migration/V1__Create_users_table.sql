CREATE TABLE "me.server.projections.users" (
  "id"       BIGSERIAL PRIMARY KEY,
  "username" VARCHAR NOT NULL,
  "password" VARCHAR NOT NULL
);