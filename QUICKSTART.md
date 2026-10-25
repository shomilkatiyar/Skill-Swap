# SkillSwap - Quick Reference Guide

## 🎯 What is SkillSwap?

A **peer-to-peer skill marketplace** where people trade knowledge instead of money. Earn credits by teaching, spend credits by learning.

---

## 💡 Core Idea

```
Teacher teaches skill → Earns credits
Learner learns skill → Spends credits
```

**No money involved** — Pure skill exchange!

---

## 🔑 Key Features at a Glance

| Feature | Purpose | Users |
|---------|---------|-------|
| **User Profiles** | Showcase offered & wanted skills | All users |
| **Skill Matching** | Find compatible skill partners | Learners |
| **Booking System** | Request & manage sessions | Both |
| **Credit Ledger** | Track earned & spent credits | All users |
| **Ratings & Reviews** | Build reputation & trust | Both |
| **Session Management** | Track session lifecycle | Both |
| **Email Notifications** | Stay informed on bookings | All users |

---

## 👥 User Journey

### Teacher Journey
```
1. Register → 2. Add skills I teach → 3. Receive booking requests
4. Accept request → 5. Earn credits → 6. Teach → 7. Receive review
```

### Learner Journey
```
1. Register → 2. Add skills I want → 3. Search for teachers
4. Request booking → 5. Wait for acceptance → 6. Learn → 7. Pay credits → 8. Leave review
```

---

## 💰 Credit System Explained

### Earning Credits (Teaching)
- Complete a teaching session → **Earn credits**
- Amount set by you (teacher)
- Example: Charge 3 credits/hour for English lessons

### Spending Credits (Learning)
- Book a learning session → **Spend credits**
- Amount set by teacher
- Example: Pay 2 credits for 1 hour of Python lessons

### Signup Bonus
- New users get **5 free credits** on registration
- Enough to book at least one session

### Example Flow
```
Day 1: Maria signs up → Gets 5 credits
Day 2: Maria teaches English → Earns 3 credits (Total: 8)
Day 3: Maria books Spanish lesson → Spends 2 credits (Total: 6)
Day 4: Maria teaches more → Earns 5 credits (Total: 11)
```

---

## 📋 Session Lifecycle

### Step 1: Request
```
Learner requests session from Teacher
↓
Credits held (not deducted yet)
```

### Step 2: Response
```
Teacher accepts ✓ or rejects ✗
```

**If Accepted:**
- Credits deducted from learner immediately
- Credits added to teacher immediately
- Session status: CONFIRMED

**If Rejected:**
- Credits released back to learner
- Session status: CANCELLED

### Step 3: Complete
```
Both parties mark session complete
↓
Prompts sent to leave reviews
```

### Step 4: Review
```
Both leave ratings (1-5 stars) + optional comment
↓
Reviews visible on both profiles
```

---

## 🌟 Reputation System

### How Ratings Work
- **Rating Scale:** 1-5 stars
- **Visibility:** Public on profile
- **Impact:** Higher rated teachers get more bookings
- **Calculation:** Average of all reviews

### Building a Good Reputation
✅ Show up on time  
✅ Teach/learn quality content  
✅ Be professional and respectful  
✅ Leave detailed reviews  
✅ Respond to messages promptly  

---

## 🔍 Finding Skills to Learn

### Method 1: Search by Skill Name
1. Go to Home page
2. Type skill name (e.g., "Spanish")
3. See all users teaching it
4. Filter by rating, level, price

### Method 2: Browse Your Wanted Skills
1. Go to Profile
2. Add skill you want to learn
3. System auto-suggests teachers
4. Browse matches

### Filtering Options
- **Skill Level:** Beginner, Intermediate, Advanced
- **Rating:** Show only 4+ star teachers
- **Credit Cost:** Show only teachers charging ≤3 credits
- **Location:** Show nearby teachers (if enabled)

---

## 📱 How to Get Started

### Step 1: Register
- Go to http://localhost:5173
- Click "Sign Up"
- Enter name, email, password
- Confirm email (auto-verified in dev)

### Step 2: Set Up Profile
- Go to Profile page
- Add bio and location (optional)
- Add skills you can teach
  - Skill name, level, credit cost
- Add skills you want to learn
  - Just skill name

### Step 3: Find a Partner
- Go to Matches page
- Search for skills you want
- Browse teachers
- Click "Request Session"

### Step 4: Book a Session
- Choose date/time
- Session moves to PENDING
- Teacher reviews request

### Step 5: Complete Session
- Both users mark complete when done
- System prompts for reviews

### Step 6: Leave Review
- Rate 1-5 stars
- Add optional comment
- Review published

---

## 🎮 Example Scenario

**Players:**
- **Alice:** Software developer, wants to learn Spanish
- **Bob:** Spanish teacher, wants to learn web development

**Timeline:**

```
Day 1:
  Alice registers → Gets 5 credits
  Bob registers → Gets 5 credits

Day 2:
  Alice adds:
    - Offered: "Web Development" (Level: Advanced, Cost: 3 credits)
    - Wanted: "Spanish"
  
  Bob adds:
    - Offered: "Spanish" (Level: Intermediate, Cost: 2 credits)
    - Wanted: "Web Development"

Day 3:
  Alice books Spanish session with Bob
  Status: PENDING
  Alice's credits: 5 (holding 2 for booking)

  Bob receives notification → Accepts booking
  Alice's credits: 3 (2 deducted and sent to Bob)
  Bob's credits: 7 (5 + 2 earned)
  Status: CONFIRMED

Day 4:
  They meet for 1-hour Spanish lesson
  Both mark complete
  Status: COMPLETED

Day 5:
  Alice leaves review: ⭐⭐⭐⭐⭐ "Great teacher! Very patient"
  Bob leaves review: ⭐⭐⭐⭐⭐ "Keen learner, asked great questions"

Day 6:
  Bob books Web Development session with Alice
  Bob's credits: 5 (7 - 2 spent)
  Alice's credits: 6 (3 + 3 earned)
  Status: CONFIRMED

Result:
  Both earned credits from teaching ✓
  Both learned new skills ✓
  Both built positive reputation ✓
```

---

## 🔐 Security & Privacy

### Your Data is Secure
- Passwords encrypted with BCrypt
- JWT tokens for authentication
- API requires login
- CORS prevents unauthorized access
- All transactions logged

### What's Visible
- Your profile (skills offered, ratings)
- Your session history
- Your reviews
- Your credit balance

### What's Private
- Your password
- Your email (only seen by you)
- Your JWT token
- Your private messages (future feature)

---

## ⚡ Quick Tips

### For Teachers
- 🎓 Set competitive credit prices to attract learners
- ⭐ Focus on getting 5-star reviews
- 💬 Write detailed reviews after sessions
- 📅 Set clear availability
- 🎯 Specialize in 3-5 skills for better matching

### For Learners
- 🔍 Read teacher reviews before booking
- ⏰ Be on time for sessions
- 📝 Ask questions and engage
- ⭐ Leave honest, detailed reviews
- 💡 Book multiple sessions to find your best fit

### For Everyone
- 👥 Build your reputation early
- 🤝 Be professional and respectful
- 📧 Check email for booking notifications
- 💰 Manage your credits wisely
- 🎓 Teach what you know well

---

## 🐛 Common Questions

**Q: What if the teacher doesn't show up?**  
A: Mark session incomplete. Contact teacher. Can leave negative review.

**Q: Can I get refunded credits?**  
A: Yes, if teacher rejects/cancels booking before session starts.

**Q: What if I have no credits?**  
A: Teach something first to earn credits, or ask friends for trades.

**Q: Can I set different prices for different skill levels?**  
A: Yes, add same skill at different levels with different prices.

**Q: How long does a session last?**  
A: You decide! Can be 30 mins, 1 hour, or longer.

**Q: Can I book multiple sessions with same teacher?**  
A: Absolutely! Build ongoing learning relationships.

**Q: What if I want to cancel?**  
A: Cancel from Sessions page (before completion) to refund credits.

---

## 🚀 Advanced Features

### Skill Levels
- **Beginner:** No prior experience needed
- **Intermediate:** Some knowledge required
- **Advanced:** Expert knowledge expected

### Session Statuses
- **PENDING:** Awaiting teacher response
- **CONFIRMED:** Teacher accepted, credits transferred
- **COMPLETED:** Session finished, reviews pending
- **CANCELLED:** Either party cancelled

### Transaction Types
- **SIGNUP_BONUS:** Credits given on registration
- **EARNING:** Credits earned from teaching
- **SPENDING:** Credits spent on learning

---

## 📞 Support & Contact

For issues or questions:
- Check PROJECT_DOCUMENTATION.md for detailed info
- Review README.md for setup instructions
- Check browser console for API errors

---

## 🎉 Benefits Summary

| For You | For Community | For Society |
|---------|---------------|-------------|
| Learn free skills | Build networks | Democratize education |
| Earn by teaching | Share knowledge | Connect people |
| Flexible schedule | Create community | Reduce education costs |
| Build reputation | Help each other | Empower everyone |
| Meet people | Exchange ideas | Break barriers |

---

**Ready to swap skills? Let's go!** 🚀

Start by registering two accounts and testing the full booking flow. Happy learning! 🌟
