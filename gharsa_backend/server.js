const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(bodyParser.json());

// MySQL Connection Config (Port 3306)
const dbConfig = {
    host: 'localhost',
    port: 3306,
    user: 'root',
    password: 'WJ28@krhps',
    multipleStatements: true
};

const db = mysql.createConnection(dbConfig);

// In-memory store for active OTP codes during development
const otpStore = {}; // { mobile: '8421' }

db.connect(err => {
    if (err) {
        console.error('========================================');
        console.error('❌ MYSQL CONNECTION FAILED ON PORT 3306!');
        console.error('Error Message:', err.message);
        console.error('========================================');
        return;
    }
    console.log('✅ Connected to MySQL Server on port 3306 successfully!');

    db.query('CREATE DATABASE IF NOT EXISTS gharsa_app_db;', (createErr) => {
        if (createErr) return;

        db.query('USE gharsa_app_db;', (useErr) => {
            if (useErr) return;

            db.query(`
                CREATE TABLE IF NOT EXISTS users (
                  email VARCHAR(255) PRIMARY KEY,
                  password VARCHAR(255) NOT NULL,
                  name VARCHAR(255) NOT NULL,
                  role VARCHAR(50) NOT NULL,
                  roomNumber VARCHAR(50) NOT NULL,
                  mobile VARCHAR(50) DEFAULT NULL
                );
            `, (tableErr) => {
                if (!tableErr) {
                    db.query("ALTER TABLE users ADD COLUMN IF NOT EXISTS mobile VARCHAR(50) DEFAULT NULL;", () => {});
                }
            });

            db.query(`
                CREATE TABLE IF NOT EXISTS tickets (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  residentEmail VARCHAR(255) NOT NULL,
                  title VARCHAR(255) NOT NULL,
                  description TEXT NOT NULL,
                  status VARCHAR(50) NOT NULL,
                  date VARCHAR(50) NOT NULL
                );
            `);

            db.query(`
                CREATE TABLE IF NOT EXISTS rents (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  residentEmail VARCHAR(255) NOT NULL,
                  amount VARCHAR(50) NOT NULL,
                  status VARCHAR(50) NOT NULL,
                  dueDate VARCHAR(50) NOT NULL,
                  month VARCHAR(50) NOT NULL
                );
            `);

            db.query(`
                INSERT IGNORE INTO users (email, password, name, role, roomNumber, mobile)
                VALUES ('sneha.roy@example.com', 'pgSuperSecure99!', 'Sneha Roy', 'RESIDENT', '304', '9876543210');
            `);
            db.query(`
                INSERT IGNORE INTO users (email, password, name, role, roomNumber, mobile)
                VALUES ('manager.rajesh@gharsa.in', 'admin123', 'Rajesh Manager', 'MANAGER', 'Office', '9123456789');
            `);

            console.log('✅ Tables (users, tickets, rents) and default demo accounts initialized.');
        });
    });
});

// 1. Login API (Email)
app.post('/api/login', (req, res) => {
    const { email, password } = req.body;
    db.query('USE gharsa_db;', () => {
        const query = 'SELECT * FROM users WHERE email = ? AND password = ?';
        db.query(query, [email, password], (err, results) => {
            if (err) return res.status(500).json({ error: err.message });
            if (results.length === 0) return res.status(401).json({ error: 'User not present' });
            res.json(results[0]);
        });
    });
});

// 2. Sign Up API
app.post('/api/signup', (req, res) => {
    const { name, email, password, role, roomNumber, mobile } = req.body;
    const cleanMobile = mobile ? mobile.trim() : '';
    db.query('USE gharsa_db;', () => {
        const query = 'INSERT INTO users (name, email, password, role, roomNumber, mobile) VALUES (?, ?, ?, ?, ?, ?)';
        db.query(query, [name, email, password, role, roomNumber, cleanMobile], (err, result) => {
            if (err) {
                console.error('❌ Sign Up SQL Error:', err.message);
                return res.status(500).json({ error: err.message });
            }
            console.log(`✅ User registered successfully: ${email} with mobile: ${cleanMobile}`);
            res.json({ email, name, role, roomNumber, mobile: cleanMobile });
        });
    });
});

// 3. Send OTP API
app.post('/api/send-otp', (req, res) => {
    const { mobile } = req.body;
    if (!mobile) return res.status(400).json({ error: 'Mobile number required' });

    const otp = "8421";
    otpStore[mobile.trim()] = otp;
    console.log(`📱 [OTP SERVICE] Sent OTP ${otp} to mobile ${mobile}`);
    res.json({ success: true, message: "OTP sent successfully", demoOtp: otp });
});

// 4. Verify OTP & Login Mobile API
app.post('/api/verify-otp', (req, res) => {
    const { mobile, otp } = req.body;
    const cleanMobile = mobile ? mobile.trim() : '';
    const cleanOtp = otp ? otp.trim() : '';

    db.query('USE gharsa_db;', () => {
        if (cleanOtp !== "8421" && (!otpStore[cleanMobile] || otpStore[cleanMobile] !== cleanOtp)) {
            return res.status(401).json({ error: 'Invalid or expired OTP' });
        }

        const query = 'SELECT * FROM users WHERE mobile = ? OR mobile LIKE ?';
        db.query(query, [cleanMobile, `%${cleanMobile}%`], (err, results) => {
            if (err) return res.status(500).json({ error: err.message });
            if (results.length === 0) {
                const defaultEmail = `${cleanMobile}@gharsa.user`;
                const defaultName = `Resident ${cleanMobile}`;
                const insertQuery = 'INSERT INTO users (email, password, name, role, roomNumber, mobile) VALUES (?, "otpLogin123", ?, "RESIDENT", "304", ?)';
                db.query(insertQuery, [defaultEmail, defaultName, cleanMobile], (insErr, insRes) => {
                    if (insErr) return res.status(500).json({ error: insErr.message });
                    db.query('SELECT * FROM users WHERE mobile = ?', [cleanMobile], (selErr, selRes) => {
                        if (selErr || selRes.length === 0) return res.status(500).json({ error: 'Auto-registration failed' });
                        delete otpStore[cleanMobile];
                        res.json(selRes[0]);
                    });
                });
            } else {
                delete otpStore[cleanMobile];
                res.json(results[0]);
            }
        });
    });
});

// 5. Update Profile API
app.post('/api/profile/update', (req, res) => {
    const { email, name, mobile, roomNumber, password } = req.body;
    db.query('USE gharsa_db;', () => {
        let query = 'UPDATE users SET name = ?, mobile = ?, roomNumber = ?';
        let params = [name, mobile, roomNumber];
        if (password && password.trim().length > 0) {
            query += ', password = ?';
            params.push(password);
        }
        query += ' WHERE email = ?';
        params.push(email);

        db.query(query, params, (err, result) => {
            if (err) return res.status(500).json({ error: err.message });
            db.query('SELECT * FROM users WHERE email = ?', [email], (selErr, selRes) => {
                if (selErr || selRes.length === 0) return res.status(500).json({ error: 'Failed to fetch updated profile' });
                res.json(selRes[0]);
            });
        });
    });
});

// 6. Get Tickets API
app.get('/api/tickets', (req, res) => {
    db.query('USE gharsa_db;', () => {
        const query = 'SELECT * FROM tickets ORDER BY id DESC';
        db.query(query, (err, results) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json(results);
        });
    });
});

// 7. Create Ticket API
app.post('/api/tickets', (req, res) => {
    const { residentEmail, title, description, date } = req.body;
    db.query('USE gharsa_db;', () => {
        const query = 'INSERT INTO tickets (residentEmail, title, description, status, date) VALUES (?, ?, ?, "IN_PROGRESS", ?)';
        db.query(query, [residentEmail, title, description, date], (err, result) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ id: result.insertId, residentEmail, title, description, status: 'IN_PROGRESS', date });
        });
    });
});

// 8. Get Rents API
app.get('/api/rents', (req, res) => {
    const { email } = req.query;
    db.query('USE gharsa_db;', () => {
        const query = 'SELECT * FROM rents WHERE residentEmail = ?';
        db.query(query, [email], (err, results) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json(results);
        });
    });
});

// 9. Pay Rent API
app.post('/api/rents/pay', (req, res) => {
    const { rentId } = req.body;
    db.query('USE gharsa_db;', () => {
        const query = 'UPDATE rents SET status = "PAID" WHERE id = ?';
        db.query(query, [rentId], (err, result) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ success: true });
        });
    });
});

// 10. Get All Tenants (role = RESIDENT) API
app.get('/api/tenants', (req, res) => {
    db.query('USE gharsa_app_db;', () => {
        const query = `
            SELECT u.*,
                   IFNULL(r.status, 'PENDING') AS rentStatus
            FROM users u
            LEFT JOIN rents r ON u.email = r.residentEmail
            WHERE u.role = 'RESIDENT'
            ORDER BY u.name ASC
        `;
        db.query(query, (err, results) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json(results);
        });
    });
});

// 11. Add Tenant API
app.post('/api/tenants/add', (req, res) => {
    const { name, phone, email, address, emergencyContact, joiningDate, roomNumber, bedNumber, monthlyRent } = req.body;
    const cleanEmail = email ? email.trim().lowercase() : `${phone}@gharsa.user`;
    const defaultPassword = 'tenantPassword123';

    db.query('USE gharsa_app_db;', () => {
        const query = `
            INSERT INTO users (email, password, name, role, roomNumber, mobile, address, emergencyContact, joiningDate, bedNumber, status, monthlyRent)
            VALUES (?, ?, ?, 'RESIDENT', ?, ?, ?, ?, ?, ?, 'ACTIVE', ?)
            ON DUPLICATE KEY UPDATE
                name=VALUES(name), mobile=VALUES(mobile), roomNumber=VALUES(roomNumber),
                address=VALUES(address), emergencyContact=VALUES(emergencyContact),
                joiningDate=VALUES(joiningDate), bedNumber=VALUES(bedNumber), monthlyRent=VALUES(monthlyRent), status='ACTIVE'
        `;
        db.query(query, [cleanEmail, defaultPassword, name, roomNumber, phone, address || '', emergencyContact || '', joiningDate || '2024-03-01', bedNumber || 'Bed A', monthlyRent || '6000'], (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            db.query(`
                INSERT INTO rents (residentEmail, amount, status, dueDate, month)
                VALUES (?, ?, 'PENDING', '5th of Month', 'Current Month')
            `, [cleanEmail, monthlyRent || '6000'], () => {});

            res.json({ success: true, email: cleanEmail });
        });
    });
});

// 12. Edit Tenant API
app.post('/api/tenants/edit', (req, res) => {
    const { email, name, phone, address, emergencyContact, joiningDate, roomNumber, bedNumber, monthlyRent, status } = req.body;

    db.query('USE gharsa_app_db;', () => {
        const query = `
            UPDATE users
            SET name = ?, mobile = ?, address = ?, emergencyContact = ?, joiningDate = ?, roomNumber = ?, bedNumber = ?, monthlyRent = ?, status = ?
            WHERE email = ?
        `;
        db.query(query, [name, phone, address, emergencyContact, joiningDate, roomNumber, bedNumber, monthlyRent, status || 'ACTIVE', email], (err, result) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ success: true });
        });
    });
});

// 13. Set Tenant Status API
app.post('/api/tenants/status', (req, res) => {
    const { email, status } = req.body;
    db.query('USE gharsa_app_db;', () => {
        const query = 'UPDATE users SET status = ? WHERE email = ?';
        db.query(query, [status, email], (err, result) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ success: true, email, status });
        });
    });
});

// 14. Get Rooms API
app.get('/api/rooms', (req, res) => {
    db.query('USE gharsa_app_db;', () => {
        const roomQuery = 'SELECT * FROM rooms ORDER BY roomNumber ASC';
        db.query(roomQuery, (err, rooms) => {
            if (err) return res.status(500).json({ error: err.message });

            const userQuery = 'SELECT email, name, roomNumber, bedNumber, mobile FROM users WHERE role = "RESIDENT" AND status = "ACTIVE"';
            db.query(userQuery, (uErr, users) => {
                if (uErr) return res.status(500).json({ error: uErr.message });

                const result = rooms.map(room => {
                    const roomResidents = users.filter(u => u.roomNumber === room.roomNumber);
                    return {
                        ...room,
                        occupiedCount: roomResidents.length,
                        residents: roomResidents
                    };
                });
                res.json(result);
            });
        });
    });
});

// 15. Add Room API
app.post('/api/rooms', (req, res) => {
    const { roomNumber, floor, capacity, rentPerBed, acType } = req.body;
    db.query('USE gharsa_app_db;', () => {
        const query = 'INSERT INTO rooms (roomNumber, floor, capacity, rentPerBed, acType) VALUES (?, ?, ?, ?, ?)';
        db.query(query, [roomNumber, floor, capacity, rentPerBed, acType || 'NON_AC'], (err, result) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ id: result.insertId, roomNumber, floor, capacity, rentPerBed, acType });
        });
    });
});

const PORT = 3000;
app.listen(PORT, '0.0.0.0', () => {
    console.log(`🚀 GharSa Backend Server running on http://0.0.0.0:${PORT}`);
});
