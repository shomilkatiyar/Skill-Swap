# SkillSwap Marketplace - Project Documentation

## 📋 Project Overview

**SkillSwap** is a peer-to-peer skill-sharing marketplace that enables users to exchange knowledge and expertise without money. The platform operates on a credit-based system where users earn credits by teaching skills and spend credits to learn from others.

### Core Concept
- **Users teach** → **Earn credits**
- **Users learn** → **Spend credits**
- **No money exchanges hands** — Only skill-for-skill barter

### Technology Stack
- **Backend:** Spring Boot 3 (Java 17), Spring Security (JWT), Spring Data JPA
- **Frontend:** React 18, Vite, Tailwind CSS, Axios
- **Database:** H2 (development), PostgreSQL (production)
- **Deployment:** Docker & docker-compose, Render/Railway (backend), Vercel/Netlify (frontend)

---

## 🎯 Key Functionalities

### 1. **User Authentication & Authorization**
- **Registration:** Email-based account creation with password validation
- **Login:** Secure JWT-based authentication
- **Token Management:** Tokens stored in browser localStorage for session persistence
- **Auto Logout:** Invalid tokens trigger automatic logout and redirect to login page
- **Password Security:** Passwords hashed using BCrypt

**Usage:** Users must create an account before accessing any marketplace features.

---

### 2. **User Profiles & Skill Management**

#### Profile Features
- **Personal Information:**
  - Name, Email, Bio, Location
  - Profile Picture Support (optional)
  - Credits Balance Display
  
- **Skills Offered (Teaching):**
  - Add skills you can teach to others
  - Set difficulty level (Beginner, Intermediate, Advanced)
  - Multiple skills can be offered simultaneously
  
- **Skills Wanted (Learning):**
  - Add skills you want to learn from others
  - Browse other users offering those skills
  - Request sessions with matching skill providers

#### Rating System
- **Average Rating:** Calculated from completed session reviews
- **Review History:** View feedback from previous session partners
- **Reputation Building:** Higher ratings attract more booking requests

**Usage:** Users build their profile to establish credibility and make their expertise discoverable.

---

### 3. **Skill Matching Algorithm**

**How It Works:**
1. User specifies a skill they want to learn
2. System searches for users offering that skill
3. Displays all matching teachers with their ratings and availability
4. User can filter by skill level, location, or rating

**Smart Matching Considers:**
- Exact skill match
- User ratings and reviews
- Credit cost (teacher sets pricing)
- Availability and schedule

**Usage:** Teachers can charge different credit amounts for different skills, and learners can find the most suitable tutor.

---

### 4. **Session Booking System**

#### Session Flow (Request → Accept/Reject → Complete → Review)

**Step 1: Create Booking Request**
- Learner chooses a teacher and requests a session
- Specifies desired time/date
- System reserves credit amount pending confirmation

**Step 2: Teacher Response**
- Teacher receives booking notification
- Options: Accept ✓ or Reject ✗
- If accepted → Credits deducted from learner immediately
- If rejected → Credits held, can be used elsewhere

**Step 3: Session Completion**
- Both users mark session as completed
- Session moves to "COMPLETED" status
- Both parties notified to leave reviews

**Step 4: Rating & Review**
- Both learner and teacher leave 5-star ratings
- Optional written feedback
- Ratings affect user credibility and visibility

**Session Statuses:**
- `PENDING` — Awaiting teacher acceptance
- `CONFIRMED` — Teacher accepted, credits transferred
- `COMPLETED` — Session finished, awaiting reviews
- `CANCELLED` — Either party declined

**Usage:** Transparent booking workflow ensures both parties are committed and accountable.

---

### 5. **Credit System (Transactional Ledger)**

#### How Credits Work
- **Signup Bonus:** New users receive 5 free credits
- **Teaching Credits:** Earned when learner completes a session with you
- **Learning Credits:** Spent when booking a session to learn from someone
- **Price Setting:** Teachers set their own credit rates per skill

#### Credit Ledger
- **Transaction History:** Complete log of all credit movements
- **Credit Balance:** Real-time balance display on user dashboard
- **Transaction Types:**
  - `SIGNUP_BONUS` — Initial credits on registration
  - `EARNING` — Credits earned from teaching sessions
  - `SPENDING` — Credits spent on learning sessions

#### Transactional Integrity
- Credit transfers happen ONLY when session is confirmed
- No partial transfers or failed transactions
- All transactions logged for audit trail

**Usage Example:**
```
User A: Wants to learn Spanish (costs 3 credits)
User B: Offers Spanish teaching (charges 3 credits)

1. User A has 10 credits, requests session from User B
2. User B accepts → User A: -3 credits (7 remaining), User B: +3 credits
3. After session completes → Both can leave reviews
```

**Usage:** Credit system creates economic incentive to teach while controlling access to learning opportunities.

---

### 6. **Email Notifications (Async)**

The system sends transactional emails for key events:

**Email Types:**
1. **Welcome Email** — Sent on registration with onboarding tips
2. **Booking Request Notification** — Teacher notified of new session request
3. **Booking Status Updates** — Learner notified when request accepted/rejected
4. **Session Reminders** — Reminder email 30 minutes before scheduled session
5. **Review Prompts** — Prompt to leave review after session completion

**Email Features:**
- **Asynchronous Processing:** Emails sent in background, don't block API
- **Graceful Degradation:** Missing SMTP configuration won't crash the app
- **Safe in Development:** Works perfectly without email setup configured

**Usage:** Keeps users informed without constant in-app notifications.

---

### 7. **Matching Recommendations**

**Smart Matching Features:**
- **Bidirectional Search:** Find teachers OR learners based on skills
- **Skill Level Matching:** Match appropriate difficulty levels
- **Availability Calendar:** See teacher availability
- **Reputation Filter:** Filter matches by minimum rating
- **Location Preference:** View nearby matches (if location enabled)

**Usage:** Helps users quickly find compatible partners for skill exchange.

---

## 📱 Frontend Features

### Pages & Components

#### **Home Page**
- Skill search and discovery
- Trending skills carousel
- Quick access to matching teachers/learners
- Navigation to all marketplace features

#### **Authentication Pages**
- **Login:** Email and password sign-in
- **Register:** Create new account with name, email, password validation

#### **Profile Page**
- View and edit personal information
- Manage skills offered (add/remove)
- Manage skills wanted (add/remove)
- View complete rating history
- Check current credit balance

#### **Matches Page**
- Browse users offering your desired skills
- Filter by skill level, rating, price
- View detailed user profiles
- Request sessions directly

#### **Sessions Page**
- View all your sessions (pending, confirmed, completed)
- Accept/reject incoming booking requests
- Mark sessions as complete
- Track session history

#### **Credits Page**
- View credit balance
- Complete transaction history
- See how credits were earned/spent
- Visualize credit trends

#### **Protected Routes**
- All pages behind login wall
- Automatic redirect to login if unauthorized
- JWT token validation

#### **Reusable Components**
- **Navbar:** Navigation, logout, credit display
- **ProtectedRoute:** Route guarding for authenticated users
- **Spinner:** Loading indicator for async operations
- **ErrorBanner:** Display error messages to users
- **EmptyState:** User-friendly empty state messages

---

## 🔒 Security Features

### Authentication
- **JWT Tokens:** Stateless authentication
- **Token Storage:** Secure localStorage management
- **CORS Configuration:** Restricts API to trusted origins
- **Password Hashing:** BCrypt encryption

### Authorization
- **Role-Based Access:** Different actions for different roles
- **Session Validation:** Only authenticated users access protected endpoints
- **Data Privacy:** Users only see their own profile and public listings

### API Security
- **Request Validation:** Input validation on all endpoints
- **Error Handling:** Global exception handler masks sensitive information
- **SQL Injection Prevention:** Parameterized queries via JPA
- **CSRF Protection:** Built into Spring Security

---

## 💾 Database Schema Overview

### Core Entities

**Users**
- id, name, email, password_hash
- bio, location, credits_balance
- created_at, updated_at

**Skills**
- id, name, description
- skill_level (BEGINNER, INTERMEDIATE, ADVANCED)
- created_at

**UserSkillOffered**
- user_id, skill_id (composite key)
- credit_cost, description
- Links users to skills they teach

**UserSkillWanted**
- user_id, skill_id (composite key)
- Links users to skills they want to learn

**Sessions**
- id, teacher_id, learner_id, skill_id
- status (PENDING, CONFIRMED, COMPLETED, CANCELLED)
- scheduled_at, credit_cost
- created_at, updated_at

**Reviews**
- id, reviewer_id, reviewee_id, session_id
- rating (1-5), comment
- created_at

**CreditTransactions**
- id, user_id, session_id
- transaction_type (SIGNUP_BONUS, EARNING, SPENDING)
- amount, created_at

---

## 🚀 Use Cases

### Use Case 1: Language Learning
**Scenario:** Maria wants to learn Spanish but can't afford expensive courses.

1. Maria registers and adds "Spanish" to her wanted skills
2. Finds Juan who teaches Spanish and charges 2 credits/hour
3. Requests a 1-hour session with Juan (2 credits needed)
4. Juan accepts → Maria spends 2 credits, Juan earns 2 credits
5. After session, both leave reviews
6. Maria can now teach English (her native language) to earn credits back

---

### Use Case 2: Freelancer Skill Exchange
**Scenario:** Alex is a graphic designer wanting to learn web development.

1. Alex adds "Graphic Design" to offered skills (charges 5 credits/hour)
2. Alex adds "Web Development" to wanted skills
3. Finds Sam who needs design work and knows web development
4. They swap: Alex teaches design, Sam teaches web dev
5. Both earn and spend credits simultaneously through different sessions
6. Build professional relationships beyond platform

---

### Use Case 3: Professional Development
**Scenario:** Carlos is a data analyst wanting to improve his public speaking.

1. Carlos registers with experience in data analysis
2. Searches for public speaking instructors
3. Books 3 sessions with different coaches to try various styles
4. Earns credits by teaching junior analysts data visualization
5. Uses earnings to fund his public speaking sessions
6. Becomes known as "expert analyst" through positive reviews
7. Gets more teaching requests, earns more credits

---

### Use Case 4: Community Learning
**Scenario:** A tech community wants to share knowledge.

1. Senior developers list technologies they can teach (Python, React, DevOps)
2. Junior developers list what they want to learn
3. Platform automatically matches compatible pairs
4. Seniors earn credits → can learn other skills
5. Juniors get mentorship access → build skills without money
6. Community grows as knowledge spreads

---

## 📊 Platform Benefits

### For Learners
✅ **Free Learning:** Use credits earned from teaching instead of paying money  
✅ **Personalized Instruction:** One-on-one sessions with real practitioners  
✅ **Flexible Scheduling:** Book sessions at convenient times  
✅ **Credible Teachers:** Ratings and reviews ensure quality  
✅ **Affordable:** No expensive course fees  

### For Teachers
✅ **Monetize Knowledge:** Earn credits by teaching  
✅ **Build Reputation:** Ratings and reviews build credibility  
✅ **Flexible Income:** Set your own rates (in credits)  
✅ **Learn Too:** Use earned credits to learn from others  
✅ **Community Building:** Connect with likeminded learners  

### For Society
✅ **Knowledge Democratization:** Make education accessible to everyone  
✅ **Peer Learning:** Build communities around shared interests  
✅ **Skill Diversity:** Encourage learning beyond traditional education  
✅ **Economic Inclusion:** Works for people with limited financial resources  
✅ **Social Connection:** Create meaningful human connections through learning  

---

## 🎯 Business Model

### Revenue Opportunities (Future)
- **Premium Subscriptions:** Unlimited bookings, priority matching
- **Skill Verification:** Badges for certified skills
- **Corporate Training:** Companies hire vetted instructors for employee training
- **Marketplace Fees:** Small commission on high-value sessions
- **Analytics Dashboard:** Insights for frequent teachers

### Current Model
- **Free Platform:** No fees, purely credit-based exchange
- **Sustainable Growth:** Network effects increase value as more users join

---

## 🔧 API Endpoints Overview

### Authentication
```
POST /api/auth/register      - Create new account
POST /api/auth/login         - Sign in with credentials
```

### Users
```
GET  /api/users/me           - Get current user profile
GET  /api/users/{id}         - Get any user's public profile
PUT  /api/users/me           - Update own profile
```

### Skills
```
GET  /api/skills             - List all skills
GET  /api/skills/{id}        - Get skill details
POST /api/users/me/skills-offered    - Add skill to teach
DELETE /api/users/me/skills-offered/{skillId}  - Remove teaching skill
POST /api/users/me/skills-wanted     - Add skill to learn
DELETE /api/users/me/skills-wanted/{skillId}   - Remove learning skill
```

### Matching
```
GET  /api/matches            - Find users offering desired skills
GET  /api/matches?skill=X&level=Y  - Filter matches
```

### Sessions
```
POST /api/sessions           - Request a session
GET  /api/sessions           - Get all user sessions
GET  /api/sessions/{id}      - Get session details
PUT  /api/sessions/{id}/status - Update session status
```

### Reviews
```
POST /api/reviews            - Leave a review
GET  /api/reviews/user/{id}  - Get user's reviews
```

### Credits
```
GET  /api/credits/balance    - Get credit balance
GET  /api/credits/history    - Get transaction history
```

---

## 📈 Success Metrics

### User Engagement
- Number of registered users
- Active users per month
- Sessions completed per user
- Average user rating

### Platform Health
- Total sessions completed
- Credit circulation (earned vs. spent)
- User retention rate
- Teaching-to-learning ratio

### Quality Assurance
- Average user rating
- Cancellation rate
- Repeat booking rate
- Review sentiment analysis

---

## 🚀 Getting Started

### For Local Development
```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend (new terminal)
cd frontend
npm install
npm run dev
```

### For Production Deployment
```bash
# Using Docker
docker compose up --build

# Or deploy to Render/Railway/Vercel/Netlify
# See README.md for detailed deployment instructions
```

### Testing the Platform
1. Register two accounts (Teacher and Learner)
2. Add matching offered/wanted skills
3. Create a booking request
4. Accept the request
5. Mark session complete
6. Leave reviews

---

## 🎓 Educational Value

This project demonstrates:
- **Full-Stack Development:** React + Spring Boot integration
- **Real-world Architecture:** Microservices patterns, async processing
- **Security Implementation:** JWT authentication, CORS, input validation
- **Database Design:** Complex relationships, transactions, audit trails
- **API Design:** RESTful principles, proper HTTP methods
- **DevOps:** Docker containerization, deployment configurations
- **User Experience:** Modern UI, error handling, loading states

---

## 📝 Future Enhancements

- **Video Conferencing:** Built-in video call integration
- **Scheduling Calendar:** Availability management
- **Skill Verification:** Certifications and badges
- **Analytics Dashboard:** Performance insights for teachers
- **Mobile App:** Native iOS/Android applications
- **AI Recommendations:** Smart matching based on learning history
- **Blockchain Credentials:** Immutable skill certificates
- **Payment Integration:** Optional paid options
- **Mentor Programs:** Long-term mentorship relationships
- **Community Events:** Group learning sessions

---

## 📄 License

This project is provided as an open-source platform for skill sharing and community learning.

---

**SkillSwap: Empowering Knowledge Exchange Without Boundaries** 🌟
