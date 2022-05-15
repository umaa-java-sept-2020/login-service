curl -X POST 'http://localhost:8085/register-user' \
  -H 'Content-Type : application/json' \
 -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4MTIzNDUiLCJleHAiOjE2NTE4Njg0MzksImlhdCI6MTY1MTg1MDQzOX0
.oSX76cMPjY1z67CeTH3cy6IFsIpqyljH8ImCOB2ayjqz3Hld1izVzgnR0UiMYWTa_K4GAyEPaxZ1vn5Khx4fUA' \
  -d '{
  "uuid": "1213",
  "username": "alimur-Interviewer",
  "password": "Password@123",
  "roles": [
    "INTERVIEWER"
  ],
  "status": "DRAFT"
}' -v