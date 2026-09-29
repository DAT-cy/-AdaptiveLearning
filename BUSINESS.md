# BUSINESS.md - Nghiệp vụ Hệ thống Adaptive Learning AI

Mục tiêu: Hệ thống luyện thi chuyên sâu theo mục tiêu. Học viên chọn chứng chỉ + điểm muốn đạt, hệ thống cho thi thử → chấm điểm → chỉ ra lỗ hổng → đề xuất gói luyện đúng dạng bài yếu.

---

## 1. Vai trò và quyền hạn

| Vai trò | Mã role | Quyền hạn |
|---------|---------|-----------|
| Học viên (Student) | `ROLE_USER` | Đăng ký, làm bài, xem kết quả, xem gap analysis, xem recommendations |
| Quản trị viên (Admin) | `ROLE_ADMIN` | Tất cả quyền của Student + quản lý certificates, topics, questions, exams, rubrics, AI jobs, practice packages |

### Tài khoản mặc định khi khởi động

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin` | ADMIN |
| `user` | `user` | USER |

### Đăng ký tài khoản mới

- Chỉ chấp nhận email `@gmail.com`
- Tự động gán `ROLE_USER`
- API: `POST /api/v1/auth/signup`

---

## 2. Chuỗi nghiệp vụ chính

### 2.1. Chuỗi 5 bước từ đầu đến cuối

```
Bước 1: ADMIN tạo nền tảng dữ liệu
  → Certificate (IELTS/TOEIC/THPT)
  → Topics (Skill → Topic → Question Type)
  → Rubric (tiêu chí chấm Writing/Speaking)
        ↓
Bước 2: ADMIN/CONTENT_EDITOR nhập câu hỏi
  → Tạo câu hỏi (DRAFT)
  → Gửi duyệt (IN_REVIEW)
  → Duyệt (APPROVED)
        ↓
Bước 3: ADMIN tạo đề thi
  → Tạo Exam (DRAFT)
  → Gắn câu hỏi vào Exam
  → Publish (PUBLISHED)
        ↓
Bước 4: STUDENT làm bài
  → Bắt đầu thi (IN_PROGRESS, có countdown)
  → Tự lưu đáp án (autosave)
  → Nộp bài (SUBMITTED → GRADED tự động cho trắc nghiệm)
  → Gửi Writing cho AI chấm (nếu có)
        ↓
Bước 5: Hệ thống phân tích + đề xuất
  → Gap Analysis (phân tích lỗ hổng theo topic/skill/difficulty)
  → Recommendations (đề xuất gói luyện tập)
  → Learning Progress (theo dõi tiến bộ theo thời gian)
```

---

## 3. Nghiệp vụ chi tiết theo từng module

### 3.1. Module Users (đã có sẵn)

#### Đăng ký

```
POST /api/v1/auth/signup
Body: { "email": "user@gmail.com", "password": "123456" }
→ Tạo user mới, role = ROLE_USER, enable = true
→ Bắt buộc email phải là @gmail.com
```

#### Đăng nhập

```
POST /api/v1/auth/login
Body: { "username": "admin", "password": "admin" }
→ Trả về { "token": "eyJ...", "username": "admin", "role": "ROLE_ADMIN" }
→ Dùng token này cho tất cả API tiếp theo: Header "Authorization: Bearer <token>"
```

#### Xem profile

```
GET /api/v1/auth/profile
→ Trả về { "username": "admin", "role": "ROLE_ADMIN" }
```

---

### 3.2. Module Certificates (Chứng chỉ)

Tạo chứng chỉ mà hệ thống hỗ trợ. Mỗi chứng chỉ có thang điểm, điểm đạt và danh sách kỹ năng.

#### Tạo chứng chỉ

```
POST /api/v1/certificates   (ADMIN)
Body:
{
  "name": "IELTS Academic",
  "code": "IELTS",
  "description": "International English Language Testing System",
  "totalScore": 9.0,
  "passingScore": 6.0,
  "passingBand": 6.5,
  "skills": "[\"LISTENING\",\"READING\",\"WRITING\",\"SPEAKING\"]"
}
→ status mặc định = DRAFT
```

#### Ví dụ chứng chỉ mẫu cần tạo

| Certificate | Code | Total Score | Passing Band | Skills |
|-------------|------|-------------|-------------|--------|
| IELTS Academic | IELTS | 9.0 | 6.5 | LISTENING, READING, WRITING, SPEAKING |
| TOEIC | TOEIC | 990 | 700 | LISTENING, READING |
| THPT Quốc Gia | THPT | 30 | 21 | TOAN, VAN, ANH, LY, HOA, SINH... |

#### Danh sách API

```
GET    /api/v1/certificates           → Xem tất cả
GET    /api/v1/certificates/{id}      → Xem chi tiết
POST   /api/v1/certificates           → Tạo mới (ADMIN)
PUT    /api/v1/certificates/{id}      → Cập nhật (ADMIN)
DELETE /api/v1/certificates/{id}      → Xóa (ADMIN)
```

---

### 3.3. Module Topics (Danh mục nội dung)

Topics tổ chức theo cấp bậc:
```
SUBJECT (IELTS)
  └── SKILL (READING)
        └── TOPIC (True/False/Not Given)
              └── DIFFICULTY (EASY, MEDIUM, HARD)
```

#### Tạo topics cho IELTS

```
POST /api/v1/topics   (ADMIN)
Body:
{
  "name": "READING",
  "code": "IELTS_READING",
  "type": "SKILL",
  "certificateId": 1,
  "sortOrder": 1
}
→ Tạo Skill READING thuộc certificate IELTS

POST /api/v1/topics   (ADMIN)
Body:
{
  "name": "True / False / Not Given",
  "code": "IELTS_READING_TFNG",
  "type": "TOPIC",
  "certificateId": 1,
  "parentId": 1,      ← parentId = topic_id của SKILL READING
  "sortOrder": 1
}
→ Tạo Topic con thuộc SKILL READING
```

#### Danh sách topics mẫu cho IELTS

| Skill | Topic | Difficulty |
|-------|-------|------------|
| READING | True/False/Not Given | EASY, MEDIUM, HARD |
| READING | Matching Headings | MEDIUM, HARD |
| READING | Multiple Choice | EASY, MEDIUM |
| READING | Summary Completion | MEDIUM, HARD |
| LISTENING | Form Completion | EASY, MEDIUM |
| LISTENING | Multiple Choice | EASY, MEDIUM |
| WRITING | Task 1 - Report | MEDIUM, HARD |
| WRITING | Task 2 - Essay | MEDIUM, HARD |
| SPEAKING | Part 1 - Interview | EASY, MEDIUM |
| SPEAKING | Part 2 - Long Turn | MEDIUM, HARD |

#### Danh sách API

```
GET    /api/v1/topics                              → Xem tất cả
GET    /api/v1/topics/certificate/{certificateId}   → Theo certificate
GET    /api/v1/topics/certificate/{id}/tree          → Cây hierarchical
GET    /api/v1/topics/type/{type}                    → Theo type (SKILL/TOPIC/...)
GET    /api/v1/topics/{id}                           → Chi tiết
GET    /api/v1/topics/{id}/children                  → Danh sách con
POST   /api/v1/topics                                → Tạo mới (ADMIN)
PUT    /api/v1/topics/{id}                           → Cập nhật (ADMIN)
DELETE /api/v1/topics/{id}                           → Soft-delete (ADMIN)
```

---

### 3.4. Module Questions (Ngân hàng câu hỏi)

#### Tạo câu hỏi trắc nghiệm (MCQ)

```
POST /api/v1/questions   (ADMIN)
Body:
{
  "certificateId": 1,
  "topicId": 2,                         ← topic_id của "True/False/Not Given"
  "questionType": "MULTIPLE_CHOICE",
  "difficulty": "MEDIUM",
  "content": "According to the passage, the author believes that...",
  "contentJson": "{\"passageRef\": \"paragraph_3\"}",
  "optionsJson": "[{\"label\":\"A\",\"text\":\"True\"},{\"label\":\"B\",\"text\":\"False\"},{\"label\":\"C\",\"text\":\"Not Given\"}]",
  "correctAnswer": "\"A\"",
  "explanation": "The passage states explicitly in paragraph 3 that...",
  "points": 1.0,
  "estimatedTimeSeconds": 90,
  "tags": "reading,tfng,medium"
}
→ status = DRAFT
```

#### Tạo câu hỏi Writing (ESSAY)

```
POST /api/v1/questions   (ADMIN)
Body:
{
  "certificateId": 1,
  "topicId": 8,                         ← topic_id của "Writing Task 2 - Essay"
  "questionType": "ESSAY",
  "difficulty": "HARD",
  "content": "Some people believe that university students should be required to attend classes. Others believe that going to classes should be optional for students. Which view do you agree with? Use specific reasons and examples to support your answer. Write at least 250 words.",
  "correctAnswer": null,                 ← tự luận, không có đáp án đúng
  "explanation": null,
  "points": 10.0,
  "estimatedTimeSeconds": 2400           ← 40 phút
}
→ status = DRAFT
```

#### Workflow trạng thái câu hỏi

```
DRAFT → submit-review → IN_REVIEW → approve → APPROVED → publish
                                                   ↓
                                               ARCHIVED
```

Quy tắc:
- Chỉ câu hỏi `APPROVED` mới được gắn vào đề thi.
- Câu `DRAFT` có thể chỉnh sửa tự do.
- Câu `IN_REVIEW` chỉ Admin mới duyệt được.

#### Danh sách API

```
GET    /api/v1/questions                          → Danh sách (filter: certificateId, topicId, difficulty, status)
GET    /api/v1/questions/{id}                     → Chi tiết
GET    /api/v1/questions/random                   → Random câu hỏi (cho mock test)
POST   /api/v1/questions                          → Tạo mới (ADMIN)
PUT    /api/v1/questions/{id}                     → Cập nhật (ADMIN)
POST   /api/v1/questions/{id}/submit-review       → Gửi duyệt
POST   /api/v1/questions/{id}/approve             → Duyệt (ADMIN)
POST   /api/v1/questions/{id}/reject              → Trả về DRAFT (ADMIN)
```

---

### 3.5. Module Exams (Đề thi)

#### Tạo đề thi

```
POST /api/v1/exams   (ADMIN)
Body:
{
  "certificateId": 1,
  "title": "IELTS Reading Practice Test 01 - True/False/Not Given",
  "description": "Bài luyện tập dạng True/False/Not Given - 10 câu",
  "durationMinutes": 20,
  "passingScore": 6.0,
  "blueprintJson": "{\"READING\":{\"TFNG\":10}}",
  "scoringJson": "{\"type\":\"raw\",\"maxScore\":10}"
}
→ status = DRAFT, totalQuestions = 0, totalPoints = 0
```

#### Gắn câu hỏi vào đề

```
POST /api/v1/exams/1/questions   (ADMIN)
Body:
{
  "questionId": 1,
  "questionOrder": 1,
  "sectionName": "Reading Part 1"
}

POST /api/v1/exams/1/questions   (ADMIN)
Body:
{
  "questionId": 2,
  "questionOrder": 2,
  "sectionName": "Reading Part 1"
}
→ Tự động cập nhật totalQuestions và totalPoints
```

#### Publish đề thi

```
POST /api/v1/exams/1/publish   (ADMIN)
→ Kiểm tra: đề phải có ít nhất 1 câu hỏi
→ status: DRAFT → PUBLISHED
→ Chỉ đề PUBLISHED mới cho học viên bắt đầu thi
```

#### Danh sách API

```
GET    /api/v1/exams                              → Danh sách (filter: certificateId)
GET    /api/v1/exams/{id}                         → Tóm tắt
GET    /api/v1/exams/{id}/detail                  → Chi tiết + danh sách câu hỏi
POST   /api/v1/exams                              → Tạo mới (ADMIN)
PUT    /api/v1/exams/{id}                         → Cập nhật (ADMIN)
DELETE /api/v1/exams/{id}                         → Xóa (ADMIN)
POST   /api/v1/exams/{id}/publish                 → Phát hành (ADMIN)
POST   /api/v1/exams/{id}/archive                 → Lưu trữ (ADMIN)
POST   /api/v1/exams/{id}/questions               → Thêm câu hỏi (ADMIN)
DELETE /api/v1/exams/{id}/questions/{questionId}  → Bỏ câu hỏi (ADMIN)
PUT    /api/v1/exams/{id}/questions/reorder       → Sắp xếp lại (ADMIN)
```

---

### 3.6. Module Attempts (Làm bài thi)

Đây là module quan trọng nhất, xử lý toàn bộ luồng làm bài của học viên.

#### Bắt đầu thi

```
POST /api/v1/attempts/start   ( STUDENT )
Body: { "examId": 1 }

Kiểm tra:
  - Exam phải có status = PUBLISHED
  - Student chưa có attempt IN_PROGRESS cho exam này

Tạo attempt:
  - startedAt = now
  - expiresAt = now + exam.durationMinutes
  - status = IN_PROGRESS
  - maxScore = exam.totalPoints

Trả về attemptId và danh sách câu hỏi (KHÔNG kèm đáp án)
```

#### Tự lưu đáp án (autosave)

```
PATCH /api/v1/attempts/{id}/answers   ( STUDENT - owner only )
Body:
{
  "questionId": 1,
  "userAnswerJson": "\"A\"",
  "timeSpentSeconds": 45
}

Kiểm tra:
  - Attempt phải là IN_PROGRESS
  - Chưa hết hạn (expiresAt > now)
  - User phải là chủ của attempt

Hành động:
  - Nếu đã có answer cho questionId → cập nhật
  - Nếu chưa có → tạo mới
  - Ghi event AUTOSAVE
```

#### Nộp bài

```
POST /api/v1/attempts/{id}/submit   ( STUDENT - owner only )
Body:
{
  "answers": [
    { "questionId": 1, "userAnswerJson": "\"A\"", "timeSpentSeconds": 45 },
    { "questionId": 2, "userAnswerJson": "\"B\"", "timeSpentSeconds": 30 }
  ]
}

Hành động:
  1. Lưu tất cả answers (final save)
  2. Ghi event SUBMIT
  3. Tự động chấm câu khách quan:
     - So sánh userAnswerJson với correctAnswer của từng câu
     - MCQ: so sánh JSON trực tiếp
     - ESSAY: isCorrect = null (chờ AI chấm)
  4. Tính điểm tổng: score = tổng pointsEarned
  5. Cập nhật attempt: status = GRADED

Trả về kết quả:
{
  "attemptId": 1,
  "score": 7.0,
  "maxScore": 10.0,
  "totalQuestions": 10,
  "correctCount": 7,
  "wrongCount": 3,
  "unansweredCount": 0,
  "timeTakenSeconds": 1200,
  "violationCount": 0
}
```

#### Ghi nhận vi phạm (anti-cheat)

```
POST /api/v1/attempts/{id}/violations   ( STUDENT - frontend tự gửi )
Body:
{
  "eventType": "TAB_SWITCH",
  "payloadJson": "{\"count\": 1}"
}

Kiểm tra:
  - Attempt phải là IN_PROGRESS

Hành động:
  - violationCount += 1
  - Ghi event với eventType
  - Nếu violationCount >= 5 → TỰ ĐỘNG NỘP BÀI, status = VIOLATED

Các loại vi phạm:
  - TAB_SWITCH: chuyển tab
  - TAB_BLUR: mất focus
  - FULLSCREEN_EXIT: thoát fullscreen
  - COPY_PASTE: copy/paste nội dung
```

#### Tính giờ

- **Nguồn chân lý:** Server (expiresAt trong DB), không phải client countdown.
- **Client countdown:** Chỉ hiển thị, hết giờ tự gọi submit.
- **Server check:** Khi submit/save, kiểm tra `now > expiresAt` → reject.
- **Auto-expire:** Background job mỗi 60 giây quét attempt `IN_PROGRESS` quá hạn → `EXPIRED`.
- **Grace period:** Hiện tại không có — hết giờ là hết.

#### Danh sách API

```
POST   /api/v1/attempts/start                       → Bắt đầu thi
GET    /api/v1/attempts/{id}                         → Chi tiết attempt + answers + timeRemaining
PATCH  /api/v1/attempts/{id}/answers                 → Tự lưu đáp án
POST   /api/v1/attempts/{id}/submit                  → Nộp bài → tự chấm
GET    /api/v1/attempts/{id}/result                  → Xem kết quả
POST   /api/v1/attempts/{id}/violations              → Ghi nhận vi phạm
GET    /api/v1/attempts/me/history                   → Lịch sử làm bài
```

---

### 3.7. Module AI (Chấm Writing/Speaking)

#### Rubric (Tiêu chí chấm)

Trước khi AI chấm, Admin cần tạo rubric cho từng kỹ năng.

```
POST /api/v1/ai/rubrics   (ADMIN)
Body:
{
  "certificateId": 1,
  "skill": "WRITING",
  "name": "IELTS Writing Task 2 Rubric",
  "description": "Rubric chấm IELTS Writing Task 2",
  "criteriaJson": "[{\"name\":\"Task Achievement\",\"weight\":25,\"desc\":\"Đáp ứng yêu cầu đề bài\"},{\"name\":\"Coherence & Cohesion\",\"weight\":25,\"desc\":\"Mạch lạc, liên kết ý\"},{\"name\":\"Lexical Resource\",\"weight\":25,\"desc\":\"Từ vựng đa dạng, chính xác\"},{\"name\":\"Grammatical Range\",\"weight\":25,\"desc\":\"Ngữ pháp đa dạng, ít lỗi\"}]"
}
```

#### Gửi bài cho AI chấm

```
POST /api/v1/ai/grade   ( STUDENT )
Body:
{
  "attemptId": 1,
  "questionId": 8,               ← câu ESSAY trong attempt
  "userAnswerText": "Some people believe that...",
  "rubricId": 1                  ← rubric WRITING
}

Hành động:
  1. Tạo AiJob (status=PENDING, type=GRADE_WRITING)
  2. Tạo AiEvaluation (status=PENDING)
  3. Gọi OpenAI API async với prompt từ rubric
  4. Parse JSON response từ AI
  5. Lưu kết quả vào AiEvaluation

Trả về:
{
  "jobId": 1,
  "status": "PENDING",
  "jobType": "GRADE_WRITING"
}
```

#### Xem kết quả AI chấm

```
GET /api/v1/ai/evaluations/attempt/{attemptId}

Trả về:
[
  {
    "evaluationId": 1,
    "overallScore": 6.5,
    "criteriaJson": "[{\"name\":\"Task Achievement\",\"score\":6,\"feedback\":\"Đủ ý nhưng thiếu ví dụ\"},{\"name\":\"Lexical Resource\",\"score\":7,\"feedback\":\"Từ vựng đa dạng\"}]",
    "errorsJson": "[{\"type\":\"GRAMMAR\",\"original\":\"He go to school\",\"correction\":\"He goes to school\",\"explanation\":\"Chủ ngữ số ít cần thêm s\"}]",
    "rewriteSuggestion": "Đoạn 2 nên viết lại: ...",
    "confidence": 0.86,
    "model": "gpt-4o-mini",
    "status": "SUCCEEDED"
  }
]
```

#### Quy trình AI chấm Writing

```
1. Nhận request: attemptId + questionId + userAnswerText + rubricId
2. Load rubric → build system prompt:
   "Bạn là giám khảo IELTS. Chấm bài theo rubric: [criteria].
    Trả về JSON: {overallScore, criteria[], errors[], strengths[], rewriteSuggestion, confidence}"
3. Gọi OpenAI API (gpt-4o-mini) qua WebClient
4. Parse JSON response → validate
5. Lưu AiEvaluation:
   - overallScore: điểm tổng 0-10
   - criteriaJson: điểm + feedback từng tiêu chí
   - errorsJson: danh sách lỗi (grammar, vocab)
   - strengthsJson: điểm mạnh
   - rewriteSuggestion: gợi ý viết lại
   - confidence: độ tin cậy 0.0-1.0
   - status: SUCCEEDED hoặc FAILED
6. Nếu FAILED → retry tối đa 3 lần (backoff 1s, 2s, 4s)
```

#### Xử lý job async

```
POST /api/v1/ai/jobs/{id}/retry   (ADMIN)
→ Retry job bị FAILED

GET /api/v1/ai/jobs               (ADMIN)
→ Xem danh sách tất cả jobs + trạng thái + token usage + cost
```

#### Danh sách API

```
POST   /api/v1/ai/grade                              → Gửi bài chấm (async)
GET    /api/v1/ai/evaluations/{id}                    → Xem 1 evaluation
GET    /api/v1/ai/evaluations/attempt/{attemptId}     → Xem tất cả evaluation của attempt
POST   /api/v1/ai/jobs/{id}/retry                     → Retry job failed (ADMIN)
GET    /api/v1/ai/jobs                                → Danh sách jobs (ADMIN)
POST   /api/v1/ai/rubrics                             → Tạo rubric (ADMIN)
PUT    /api/v1/ai/rubrics/{id}                        → Cập nhật rubric (ADMIN)
GET    /api/v1/ai/rubrics/certificate/{certificateId} → Danh sách rubric
```

---

### 3.8. Module Analytics (Phân tích lỗ hổng)

#### Phân tích sau mỗi bài thi

```
POST /api/v1/analytics/attempts/{attemptId}/analyze   ( STUDENT )

Hành động:
  1. Load tất cả AttemptAnswer của attempt
  2. Tính accuracy = correct / total * 100
  3. Phân loại theo threshold:
     - < 40%  → CRITICAL (lỗ hổng nghiêm trọng)
     - 40-60% → WEAK (yếu)
     - 60-80% → DEVELOPING (đang phát triển)
     - > 80%  → STRONG (mạnh)
  4. Lưu GapAnalysis (accuracy by skill/topic/difficulty)
  5. Tự动生成 Recommendations:
     - accuracy < 40%  → priority = HIGH, reason = "Critical gaps..."
     - accuracy < 60%  → priority = HIGH, reason = "Weak areas..."
     - accuracy < 80%  → priority = MEDIUM, reason = "Developing..."

Trả về:
{
  "gapAnalysisId": 1,
  "overallAccuracy": 55.0,
  "weakTopicsJson": "[\"overall\"]",
  "strongTopicsJson": "[]"
}
```

#### Xem tiến bộ

```
GET /api/v1/analytics/me/progress?certificateId=1   ( STUDENT )

Trả về:
{
  "userId": 2,
  "certificateId": 1,
  "averageScore": 65.0,
  "totalAttempts": 5,
  "masteryBySkillJson": "{\"READING\":70,\"LISTENING\":60,\"WRITING\":50}",
  "scoreTrendJson": "[{\"attempt\":1,\"score\":50},{\"attempt\":2,\"score\":55}]"
}
```

#### Xem recommendations

```
GET /api/v1/analytics/me/recommendations   ( STUDENT )

Trả về danh sách đề xuất:
[
  {
    "recommendationId": 1,
    "practicePackageId": null,
    "reason": "Performance below 60% indicates weak areas that need focused practice.",
    "priority": "HIGH",
    "status": "PENDING"
  }
]
```

#### Danh sách API

```
POST   /api/v1/analytics/attempts/{attemptId}/analyze  → Chạy phân tích
GET    /api/v1/analytics/attempts/{attemptId}/analysis  → Xem kết quả phân tích
GET    /api/v1/analytics/me/progress                     → Tổng quan tiến bộ
GET    /api/v1/analytics/me/recommendations              → Danh sách đề xuất
POST   /api/v1/analytics/recommendations/{id}/start      → Bắt đầu luyện gói
POST   /api/v1/analytics/recommendations/{id}/complete   → Hoàn thành luyện gói
```

---

### 3.9. Module Practice (Gói luyện tập)

#### Tạo gói luyện

```
POST /api/v1/practice   (ADMIN)
Body:
{
  "title": "Gói cứu điểm True/False/Not Given - 10 câu dễ",
  "description": "Luyện tập dạng TFNG với độ khó EASY",
  "certificateId": 1,
  "targetTopicId": 2,                  ← topic_id của TFNG
  "targetDifficulty": "EASY",
  "questionIdsJson": "[1,3,5,7,9,11,13,15,17,19]",
  "estimatedMinutes": 15
}
→ Tự động tính questionCount = 10
```

#### Danh sách API

```
GET    /api/v1/practice                      → Danh sách (filter: certificateId)
GET    /api/v1/practice/{id}                  → Chi tiết
POST   /api/v1/practice                       → Tạo mới (ADMIN)
PUT    /api/v1/practice/{id}                  → Cập nhật (ADMIN)
DELETE /api/v1/practice/{id}                  → Xóa (ADMIN)
```

---

## 4. Chuỗi ví dụ cụ thể: Từ 0 đến có kết quả

### Bước 1: Đăng nhập Admin

```
POST /api/v1/auth/login
{ "username": "admin", "password": "admin" }
→ token = "eyJhbGciOiJIUzI1NiJ9..."
```

### Bước 2: Tạo Certificate IELTS

```
POST /api/v1/certificates
Headers: Authorization: Bearer eyJ...
{
  "name": "IELTS Academic",
  "code": "IELTS",
  "totalScore": 9.0,
  "passingScore": 6.0,
  "passingBand": 6.5,
  "skills": "[\"LISTENING\",\"READING\",\"WRITING\",\"SPEAKING\"]"
}
→ certificateId = 1
```

### Bước 3: Tạo Topics

```
POST /api/v1/topics
{ "name": "READING", "code": "IELTS_R", "type": "SKILL", "certificateId": 1 }
→ topicId = 1 (SKILL)

POST /api/v1/topics
{ "name": "True/False/Not Given", "code": "IELTS_R_TFNG", "type": "TOPIC", "certificateId": 1, "parentId": 1 }
→ topicId = 2 (TOPIC)
```

### Bước 4: Tạo Rubric Writing

```
POST /api/v1/ai/rubrics
{
  "certificateId": 1,
  "skill": "WRITING",
  "name": "IELTS Writing Task 2",
  "criteriaJson": "[{\"name\":\"Task Achievement\",\"weight\":25},{\"name\":\"Coherence\",\"weight\":25},{\"name\":\"Lexical\",\"weight\":25},{\"name\":\"Grammar\",\"weight\":25}]"
}
→ rubricId = 1
```

### Bước 5: Tạo 5 câu hỏi TFNG

```
POST /api/v1/questions
{ "certificateId": 1, "topicId": 2, "questionType": "MULTIPLE_CHOICE", "difficulty": "EASY", "content": "Câu 1: The passage states...", "optionsJson": "[{\"label\":\"T\",\"text\":\"True\"},{\"label\":\"F\",\"text\":\"False\"},{\"label\":\"NG\",\"text\":\"Not Given\"}]", "correctAnswer": "\"T\"", "points": 1.0 }

→ Lặp lại 5 lần với nội dung khác nhau
→ Duyệt: POST /api/v1/questions/{id}/approve  cho từng câu
```

### Bước 6: Tạo Exam

```
POST /api/v1/exams
{ "certificateId": 1, "title": "IELTS Reading - TFNG Practice 01", "durationMinutes": 15, "passingScore": 3.0 }
→ examId = 1

POST /api/v1/exams/1/questions
{ "questionId": 1, "questionOrder": 1 }
→ Lặp lại cho 5 câu

POST /api/v1/exams/1/publish
→ status = PUBLISHED
```

### Bước 7: Học viên đăng nhập và thi

```
POST /api/v1/auth/login
{ "username": "user", "password": "user" }
→ token = "eyJ..."

POST /api/v1/attempts/start
{ "examId": 1 }
→ attemptId = 1, expiresAt = now + 15 phút

PATCH /api/v1/attempts/1/answers
{ "questionId": 1, "userAnswerJson": "\"T\"", "timeSpentSeconds": 30 }

→ Lặp lại cho 5 câu

POST /api/v1/attempts/1/submit
{ "answers": [...] }
→ score = 4.0, correctCount = 4, wrongCount = 1
```

### Bước 8: Chấm Writing bằng AI

```
POST /api/v1/ai/grade
{ "attemptId": 1, "questionId": 8, "userAnswerText": "Some people believe...", "rubricId": 1 }
→ jobId = 1

GET /api/v1/ai/evaluations/attempt/1
→ overallScore = 6.5, errors = [...], rewriteSuggestion = "..."
```

### Bước 9: Phân tích lỗ hổng

```

POST /api/v1/analytics/attempts/1/analyze

→ overallAccuracy = 55.0

→ recommendation: "Weak areas need focused practice", priority: HIGH


GET /api/v1/analytics/me/progress?certificateId=1

→ averageScore, scoreTrend, masteryBySkill
```

### Bước 10: Hỏi AI Tutor về câu sai

```
POST /api/v1/tutor/conversations
{ "certificateId": 1, "title": "Hỏi về câu 3 TFNG" }
→ conversationId = 1

POST /api/v1/tutor/conversations/1/messages
{ "content": "Câu 3 tôi chọn B nhưng đáp án là A. Tại sao?", "contextJson": "{\"questionId\":3,\"correctAnswer\":\"A\",\"userAnswer\":\"B\"}" }
→ AI trả lời: "Đáp án đúng là A vì đoạn văn ở paragraph 2 nói rằng..."
```

### Bước 11: AI sinh câu hỏi mới

```
POST /api/v1/content-generation
{ "certificateId": 1, "topicId": 2, "questionType": "MULTIPLE_CHOICE", "difficulty": "MEDIUM", "count": 5, "instructions": "Generate IELTS TFNG questions about technology topic" }
→ generationId = 1, status = READY, itemContent = [5 câu JSON]

POST /api/v1/content-generation/1/apply
→ Tạo 5 câu DRAFT trong question bank, teacher review + approve
```

### Bước 12: Nộp bài Speaking

```
POST /api/v1/speaking/submissions
{ "questionId": 10, "transcriptText": "Well, I think technology has both advantages and disadvantages...", "durationSeconds": 120 }
→ submissionId = 1

POST /api/v1/speaking/evaluations
{ "submissionId": 1, "rubricId": 2 }
→ overallScore = 6.0, errors = [...], feedback = "Fluency is good but grammar needs improvement"
```

### Bước 13: Upload tài liệu cho RAG

```
POST /api/v1/rag/documents
{ "title": "IELTS Writing Tips", "certificateId": 1, "content": "When writing Task 2, always structure your essay..." }
→ documentId = 1, chunkCount = 3

GET /api/v1/rag/documents/1/search?query=essay structure
→ returns relevant chunks with highlight
```

### Bước 14: Tạo lộ trình học AI

```
POST /api/v1/plans
{ "certificateId": 1, "targetScore": 7.0, "examDate": "2026-12-15", "hoursPerWeek": 10, "title": "IELTS 7.0 trong 2 tháng" }
→ planId = 1, phân bổ task theo ngày cho đến exam date

GET /api/v1/plans/1/summary
→ progressPercent = 0, taskCount = 45, skillDistribution = {"READING":30%,"WRITING":25%}
```

---

## 4A. Sáu module AI mở rộng cho dự án 3 tháng

MVP ban đầu đã có AI grading, analytics và recommendations. Để dự án có chiều sâu AI hơn, bổ sung 6 module sau. Mỗi module có dữ liệu riêng, API riêng và giao tiếp qua service interface; không để controller gọi trực tiếp AI provider.

### 4A.1. `tutor` - AI Tutor hội thoại

Mục tiêu: cho phép học viên hỏi về câu sai, bài viết, rubric và tài liệu học. AI phải trả lời có căn cứ, trích dẫn context đã cung cấp và không tự bịa đáp án.

Luồng nghiệp vụ:

1. Student tạo conversation, có thể gắn certificate.
2. Student gửi message và context của câu hỏi/bài làm.
3. Backend lưu message USER.
4. Tutor lấy tối đa 10 message gần nhất, ghép context và gọi `AiProvider`.
5. Backend lưu message ASSISTANT cùng citations JSON.
6. Student xem lịch sử hoặc archive conversation.

API:

```text
POST   /api/v1/tutor/conversations
GET    /api/v1/tutor/conversations
GET    /api/v1/tutor/conversations/{id}/messages
POST   /api/v1/tutor/conversations/{id}/messages
DELETE /api/v1/tutor/conversations/{id}
```

Quy tắc AI:

- Chỉ sử dụng context được truyền vào hoặc tài liệu đã được retrieve.
- Nếu không đủ context, phải nói rõ "chưa đủ dữ liệu".
- Lưu role `USER/ASSISTANT`, model, token count và citations.
- Không gửi JWT/password hoặc dữ liệu nhạy cảm vào prompt.

### 4A.2. `contentgen` - AI sinh câu hỏi và đề luyện

Mục tiêu: Teacher/Admin nhập topic, loại câu hỏi, độ khó và số lượng; AI sinh bản nháp để giảm thời gian biên soạn.

Luồng nghiệp vụ:

1. Admin/Editor gửi yêu cầu sinh nội dung.
2. Tạo `ContentGeneration` ở trạng thái `PENDING`.
3. AI sinh JSON bắt buộc gồm content, options, correctAnswer, explanation và metadata.
4. Validate schema, số lượng, đáp án và nội dung trùng lặp.
5. Chuyển `READY`; người dùng xem bản nháp.
6. Chọn Apply để đưa vào question bank ở trạng thái `DRAFT` hoặc `IN_REVIEW`; không publish tự động.

API:

```text
POST /api/v1/content-generation
GET  /api/v1/content-generation
GET  /api/v1/content-generation/{id}
POST /api/v1/content-generation/{id}/apply
```

Trạng thái: `PENDING -> PROCESSING -> READY -> APPLIED`, lỗi chuyển `FAILED`. Nội dung AI sinh ra bắt buộc phải được con người duyệt.

### 4A.3. `speaking` - AI chấm Speaking

MVP nhận transcript text; giai đoạn tiếp theo nhận audio qua object storage và chạy speech-to-text.

Luồng nghiệp vụ:

1. Student nộp transcript/audio cho câu Speaking.
2. Tạo `SpeakingSubmission` với status `UPLOADED`.
3. Nếu có audio: chạy STT, lưu transcript và timestamp.
4. Gọi rubric Speaking để chấm fluency, pronunciation, lexical resource, grammar và coherence.
5. Lưu `SpeakingEvaluation` với criteria, lỗi, strengths, rewrite và confidence.

API:

```text
POST /api/v1/speaking/submissions
GET  /api/v1/speaking/submissions/{id}
GET  /api/v1/speaking/submissions/attempt/{attemptId}
POST /api/v1/speaking/evaluations
GET  /api/v1/speaking/evaluations/{id}
GET  /api/v1/speaking/evaluations/submission/{submissionId}
```

Status submission: `UPLOADED -> TRANSCRIBING -> GRADING -> GRADED`, lỗi `FAILED`. Không suy luận thuộc tính nhạy cảm từ giọng nói.

### 4A.4. `adaptive` - Adaptive Engine

Mục tiêu: xây dựng hồ sơ năng lực theo kỹ năng và đề xuất độ khó tiếp theo.

Luồng nghiệp vụ:

1. Sau mỗi attempt, lấy điểm, đáp án, skill và difficulty.
2. Cập nhật `LearnerProfile.abilityBySkillJson` theo weighted accuracy.
3. Skill yếu nhất được ưu tiên luyện.
4. Chọn difficulty: EASY nếu năng lực < 0.4, MEDIUM nếu 0.4-0.7, HARD nếu > 0.7.
5. Forecast điểm mục tiêu dựa trên mastery, lịch sử và certificate scale.

API:

```text
GET  /api/v1/adaptive/profile?certificateId={id}
POST /api/v1/adaptive/attempts/{attemptId}/update
GET  /api/v1/adaptive/recommend?certificateId={id}
GET  /api/v1/adaptive/forecast?certificateId={id}
```

Giai đoạn đầu dùng heuristic có thể giải thích; giai đoạn sau thay bằng IRT/Bayesian Knowledge Tracing nhưng vẫn giữ model version và fallback.

### 4A.5. `rag` - Document RAG

Mục tiêu: cho phép người dùng đưa giáo trình/ghi chú vào hệ thống để Tutor trả lời dựa trên tài liệu riêng.

Luồng nghiệp vụ:

1. Student upload file hoặc paste Markdown/text.
2. Backend lưu `StudyDocument` và chia nội dung thành chunk khoảng 500 ký tự.
3. Mỗi chunk lưu documentId, index, content và keywords.
4. Search trả về các chunk liên quan; giai đoạn nâng cao thay keyword search bằng embedding/vector database.
5. Tutor nhận các chunk được retrieve làm context, trả lời có citation.

API:

```text
POST   /api/v1/rag/documents
GET    /api/v1/rag/documents
GET    /api/v1/rag/documents/{id}
GET    /api/v1/rag/documents/{id}/search?query={text}
DELETE /api/v1/rag/documents/{id}
```

Status: `UPLOADED -> PROCESSING -> READY`, lỗi `FAILED`. Chỉ owner được đọc/xóa tài liệu; không đưa tài liệu của user này vào context của user khác.

### 4A.6. `planner` - AI Study Planner

Mục tiêu: tạo kế hoạch học theo target score, exam date, số giờ/tuần và các skill yếu.

Luồng nghiệp vụ:

1. Student nhập certificate, điểm mục tiêu, ngày thi và số giờ mỗi tuần.
2. Planner tạo `StudyPlan` và phân bổ `PlanTask` theo ngày.
3. Task gồm STUDY, PRACTICE, MOCK_TEST, REVIEW, WRITING hoặc SPEAKING.
4. Sau mỗi gap analysis/adaptive update, kế hoạch có thể được tạo version mới.
5. Student hoàn thành task; hệ thống cập nhật progress và task quá hạn.

API:

```text
POST  /api/v1/plans
GET   /api/v1/plans
GET   /api/v1/plans/{id}
GET   /api/v1/plans/{id}/summary
PATCH /api/v1/plans/tasks/{taskId}
POST  /api/v1/plans/{id}/complete
POST  /api/v1/plans/{id}/archive
```

Status plan: `ACTIVE -> COMPLETED` hoặc `ACTIVE -> ARCHIVED`; task: `PENDING -> COMPLETED/SKIPPED`.

### 4A.7. Quan hệ giữa các module AI

```text
Attempt
  -> AI Grading (Writing/Speaking)
  -> Gap Analysis
  -> Adaptive Profile
  -> Recommendations
  -> Study Planner

Study Documents
  -> RAG Search
  -> AI Tutor citations

Content Generation
  -> Human Review
  -> Question Bank
  -> Exam
```

---

## 5. Quy tắc nghiệp vụ quan trọng

### Quy tắc chấm điểm

| Loại câu hỏi | Cách chấm | Người chấm |
|--------------|-----------|------------|
| MULTIPLE_CHOICE | So sánh userAnswerJson với correctAnswer (JSON equals) | Hệ thống (auto) |
| TRUE_FALSE_NOT_GIVEN | So sánh JSON | Hệ thống (auto) |
| FILL_BLANK | So sánh text (case-insensitive) | Hệ thống (auto) |
| ESSAY | Gọi AI chấm theo rubric | AI (async) |

### Quy tắc chống gian lận

| Ngưỡng | Hành động |
|--------|-----------|
| violationCount = 1-3 | Ghi nhận, không làm gì |
| violationCount = 4 | Ghi nhận, frontend cảnh báo |
| violationCount >= 5 | Tự động nộp bài, status = VIOLATED |

### Quy tắc phân tích lỗ hổng

| Accuracy | Mức độ | Hành động |
|----------|--------|-----------|
| < 40% | CRITICAL | Recommendation HIGH, lý do critical |
| 40-60% | WEAK | Recommendation HIGH, lý do weak |
| 60-80% | DEVELOPING | Recommendation MEDIUM |
| > 80% | STRONG | Không tạo recommendation |

### Quy tắc trạng thái

| Đối tượng | Trạng thái hợp lệ |
|-----------|-------------------|
| Question | DRAFT → IN_REVIEW → APPROVED → ARCHIVED |
| Exam | DRAFT → PUBLISHED → ARCHIVED |
| Attempt | IN_PROGRESS → SUBMITTED → GRADING → GRADED |
| Attempt (vi phạm) | IN_PROGRESS → VIOLATED |
| Attempt (hết giờ) | IN_PROGRESS → EXPIRED |
| AiJob | PENDING → PROCESSING → SUCCEEDED / FAILED / RETRYING |
| Recommendation | PENDING → STARTED → COMPLETED |

---

## 6. Cấu trúc module hóa code

```
adaptivelearning/
├── config/                     # Security, JWT, Swagger, Exception, Response
├── shared/enums/               # 9 enum dùng chung
├── module/
│   ├── users/                  # Auth, profile, user management
│   ├── certificates/           # Certificate CRUD
│   ├── topics/                 # Topic tree hierarchy
│   ├── questions/              # Question bank + workflow duyệt
│   ├── exams/                  # Exam + exam-question linking
│   ├── attempts/               # Timer, autosave, submit, scoring, anti-cheat
│   ├── ai/                     # AI gateway, grading, rubrics, jobs
│   ├── analytics/              # Gap analysis, recommendations, progress
│   └── practice/               # Practice packages
└── infrastructure/             # External AI provider, storage (tương lai)
```

Mỗi module: `controller/`, `service/`, `service/impl/`, `repository/`, `entity/`, `dto/request/`, `dto/response/`.

---

## 7. Swagger UI

Sau khi chạy app, truy cập:

| Mục | URL |
|-----|-----|
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |

Cách test:
1. Login bằng `POST /api/v1/auth/login` → lấy token
2. Trên Swagger, bấm nút **Authorize** → nhập `Bearer <token>`
3. Thử các API theo thứ tự trong section 4

---

## 8. Lộ trình 3 tháng

### Tháng 1 (Tuần 1-4): Nền tảng + Mock Test Engine

- **Tuần 1:** Docker MySQL, boot app, login admin/admin, tạo Certificate IELTS + Topics mẫu
- **Tuần 2:** Nhập 20-30 câu hỏi TFNG/MCQ mẫu, duyệt, tạo Exam + Publish
- **Tuần 3:** Login student/user, làm bài thi, autosave, submit, scoring auto hoạt động
- **Tuần 4:** Timer, anti-cheat violations, expire job, gap analysis cơ bản
- **Deliverable:** Student có thể thi mock test IELTS cơ bản với kết quả chi tiết

### Tháng 2 (Tuần 5-8): AI Grading + Content Generation + Speaking

- **Tuần 5:** Set AI_API_KEY, tạo Rubric WRITING, thi xong gọi AI grading, verify evaluation result
- **Tuần 6:** Hoàn thiện Speaking module: transcript → AI chấm rubric → errors/feedback
- **Tuần 7:** Content Generation: admin yêu cầu AI sinh 10 câu TFNG → review → apply vào question bank
- **Tuần 8:** AI Tutor: học viên hỏi "Tại sao đáp án B sai?" → AI trả lời có citations
- **Deliverable:** AI chấm Writing + Speaking hoạt động, AI sinh câu hỏi, AI Tutor trả lời câu hỏi

### Tháng 3 (Tuần 9-12): Adaptive Engine + RAG + Study Planner

- **Tuần 9:** Document RAG: upload giáo trình → chunk → search → Tutor dùng context từ documents
- **Tuần 10:** Adaptive Engine: learner profile, updateProfile sau attempt, recommendNextDifficulty
- **Tuần 11:** Study Planner: student nhập target + exam date → AI tạo lịch học + phân bổ task
- **Tuần 12:** Tích hợp tổng thể: thi → AI chấm → gap analysis → adaptive update → planner → recommendations
- **Deliverable:** Chuỗi khép kín từ onboarding đến adaptive learning + AI Tutor + RAG

### Tuần 4: Gap Analysis + Recommendations

- [ ] Sau submit → chạy analyze
- [ ] Kiểm tra gap analysis result
- [ ] Kiểm tra recommendations tự tạo
- [ ] Tạo practice packages mẫu
- [ ] Test progress overview
