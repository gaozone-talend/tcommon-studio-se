// $ANTLR : "SQL92SelectParser.g" -> "Sql92SelectLexer.java"$

package com.quantum.sql.grammar;

public interface Sql92SelectParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int TABLE_IDENTIFIER = 4;
	int COLUMN_IDENTIFIER = 5;
	int COLUMN_IDENTIFIER_ALIAS = 6;
	int ALIAS_IDENTIFIER = 7;
	int CONDITION_COLUMN_IDENTIFIER = 8;
	int CONDITION_TABLE_IDENTIFIER = 9;
	int SEMICOLON = 10;
	int SELECT = 11;
	int ALL = 12;
	int DISTINCT = 13;
	int COMMA = 14;
	int STAR = 15;
	int LITERAL_COUNT = 16;
	int LPAREN = 17;
	int RPAREN = 18;
	int LITERAL_MAX = 19;
	int LITERAL_MIN = 20;
	int LITERAL_AVG = 21;
	int LITERAL_SUM = 22;
	int DOT_STAR = 23;
	int AS = 24;
	int FROM = 25;
	int Variable = 26;
	int CROSS = 27;
	int JOIN = 28;
	int INNER = 29;
	int LEFT = 30;
	int RIGHT = 31;
	int FULL = 32;
	int OUTER = 33;
	int ON = 34;
	int DOT = 35;
	int AND = 36;
	int OR = 37;
	int WHERE = 38;
	int NOT = 39;
	int SOME = 40;
	int ANY = 41;
	int IS = 42;
	int NULL = 43;
	int LIKE = 44;
	int ESCAPE = 45;
	int BETWEEN = 46;
	int IN = 47;
	int EXISTS = 48;
	int ORDER = 49;
	int BY = 50;
	int ASC = 51;
	int DESC = 52;
	int Integer = 53;
	int Real = 54;
	int HexLiteral = 55;
	int Currency = 56;
	int ODBCDateTime = 57;
	int PLUS = 58;
	int MINUS = 59;
	int TILDE = 60;
	int DIVIDE = 61;
	int MOD = 62;
	int AMPERSAND = 63;
	int BITWISEOR = 64;
	int BITWISEXOR = 65;
	int ASSIGNEQUAL = 66;
	int NOTEQUAL1 = 67;
	int NOTEQUAL2 = 68;
	int LESSTHANOREQUALTO1 = 69;
	int LESSTHANOREQUALTO2 = 70;
	int LESSTHAN = 71;
	int GREATERTHANOREQUALTO1 = 72;
	int GREATERTHANOREQUALTO2 = 73;
	int GREATERTHAN = 74;
	int UNION = 75;
	int NonQuotedIdentifier = 76;
	int QuotedIdentifier = 77;
	int UnicodeStringLiteral = 78;
	int ASCIIStringLiteral = 79;
	int GROUP = 80;
	int HAVING = 81;
	int ADD = 82;
	int ALTER = 83;
	int AUTHORIZATION = 84;
	int AUTO = 85;
	int BACKUP = 86;
	int BASE64 = 87;
	int BEGIN = 88;
	int BINARY = 89;
	int BREAK = 90;
	int BROWSE = 91;
	int BULK = 92;
	int CASCADE = 93;
	int CASE = 94;
	int CAST = 95;
	int CHECK = 96;
	int CHECKPOINT = 97;
	int CLOSE = 98;
	int CLUSTERED = 99;
	int COLLATE = 100;
	int COLUMN = 101;
	int COMMIT = 102;
	int COMPUTE = 103;
	int CONCAT = 104;
	int CONSTRAINT = 105;
	int CONTAINS = 106;
	int CONTAINSTABLE = 107;
	int CONTINUE = 108;
	int CREATE = 109;
	int CUBE = 110;
	int CURRENT = 111;
	int CURRENT_DATE = 112;
	int CURRENT_TIME = 113;
	int CURRENT_TIMESTAMP = 114;
	int CURRENT_USER = 115;
	int CURSOR = 116;
	int DATABASE = 117;
	int DBCC = 118;
	int DEALLOCATE = 119;
	int DECLARE = 120;
	int DEFAULT = 121;
	int DELETE = 122;
	int DENY = 123;
	int DISK = 124;
	int DISTRIBUTED = 125;
	int DOUBLE = 126;
	int DROP = 127;
	int DUMP = 128;
	int ELEMENTS = 129;
	int ELSE = 130;
	int END = 131;
	int ERRLVL = 132;
	int EXCEPT = 133;
	int EXEC = 134;
	int EXECUTE = 135;
	int EXIT = 136;
	int EXPAND = 137;
	int EXPLICIT = 138;
	int FAST = 139;
	int FASTFIRSTROW = 140;
	int FETCH = 141;
	int FILE = 142;
	int FILLFACTOR = 143;
	int FOR = 144;
	int FORCE = 145;
	int FOREIGN = 146;
	int FREETEXT = 147;
	int FREETEXTTABLE = 148;
	int FUNCTION = 149;
	int GOTO = 150;
	int GRANT = 151;
	int HASH = 152;
	int HOLDLOCK = 153;
	int IDENTITY = 154;
	int IDENTITY_INSERT = 155;
	int IDENTITYCOL = 156;
	int IF = 157;
	int INDEX = 158;
	int INSERT = 159;
	int INTERSECT = 160;
	int INTO = 161;
	int KEEP = 162;
	int KEEPFIXED = 163;
	int KEY = 164;
	int KILL = 165;
	int LINENO = 166;
	int LOAD = 167;
	int LOOP = 168;
	int MAXDOP = 169;
	int MERGE = 170;
	int NATIONAL = 171;
	int NOCHECK = 172;
	int NOLOCK = 173;
	int NONCLUSTERED = 174;
	int OF = 175;
	int OFF = 176;
	int OFFSETS = 177;
	int OPEN = 178;
	int OPENDATASOURCE = 179;
	int OPENQUERY = 180;
	int OPENROWSET = 181;
	int OPENXML = 182;
	int OPTION = 183;
	int OVER = 184;
	int PAGLOCK = 185;
	int PERCENT = 186;
	int PLAN = 187;
	int PRECISION = 188;
	int PRIMARY = 189;
	int PRINT = 190;
	int PROC = 191;
	int PROCEDURE = 192;
	int PUBLIC = 193;
	int RAISERROR = 194;
	int RAW = 195;
	int READ = 196;
	int READCOMMITED = 197;
	int READPAST = 198;
	int READTEXT = 199;
	int READUNCOMMITED = 200;
	int RECONFIGURE = 201;
	int REFERENCES = 202;
	int REMOTE = 203;
	int REPEATABLEREAD = 204;
	int REPLICATION = 205;
	int RESTORE = 206;
	int RESTRICT = 207;
	int RETURN = 208;
	int REVOKE = 209;
	int ROBUST = 210;
	int ROLLBACK = 211;
	int ROLLUP = 212;
	int ROWCOUNT = 213;
	int ROWGUIDCOL = 214;
	int ROWLOCK = 215;
	int RULE = 216;
	int SAVE = 217;
	int SCHEMA = 218;
	int SERIALIZABLE = 219;
	int SESSION_USER = 220;
	int SET = 221;
	int SETUSER = 222;
	int SHUTDOWN = 223;
	int STATISTICS = 224;
	int SYSTEM_USER = 225;
	int TABLE = 226;
	int TABLOCK = 227;
	int TABLOCKX = 228;
	int TEXTSIZE = 229;
	int THEN = 230;
	int TIES = 231;
	int TO = 232;
	int TOP = 233;
	int TRAN = 234;
	int TRANSACTION = 235;
	int TRIGGER = 236;
	int TRUNCATE = 237;
	int TSEQUAL = 238;
	int UNIQUE = 239;
	int UPDATE = 240;
	int UPDATETEXT = 241;
	int UPDLOCK = 242;
	int USE = 243;
	int USER = 244;
	int VALUES = 245;
	int VARYING = 246;
	int VIEW = 247;
	int VIEWS = 248;
	int WAITFOR = 249;
	int WHEN = 250;
	int WHILE = 251;
	int WITH = 252;
	int WRITETEXT = 253;
	int XLOCK = 254;
	int XML = 255;
	int XMLDATA = 256;
	int F_CONNECTIONS = 257;
	int F_CPU_BUSY = 258;
	int F_CURSOR_ROWS = 259;
	int F_DATEFIRST = 260;
	int F_DBTS = 261;
	int F_ERROR = 262;
	int F_FETCH_STATUS = 263;
	int F_IDENTITY = 264;
	int F_IDLE = 265;
	int F_IO_BUSY = 266;
	int F_LANGID = 267;
	int F_LANGUAGE = 268;
	int F_LOCK_TIMEOUT = 269;
	int F_MAX_CONNECTIONS = 270;
	int F_MAX_PRECISION = 271;
	int F_NESTLEVEL = 272;
	int F_OPTIONS = 273;
	int F_PACK_RECEIVED = 274;
	int F_PACK_SENT = 275;
	int F_PACKET_ERRORS = 276;
	int F_PROCID = 277;
	int F_REMSERVER = 278;
	int F_ROWCOUNT = 279;
	int F_SERVERNAME = 280;
	int F_SERVICENAME = 281;
	int F_SPID = 282;
	int F_TEXTSIZE = 283;
	int F_TIMETICKS = 284;
	int F_TOTAL_ERRORS = 285;
	int F_TOTAL_READ = 286;
	int F_TOTAL_WRITE = 287;
	int F_TRANCOUNT = 288;
	int F_VERSION = 289;
	int COLON = 290;
	int Whitespace = 291;
	int SingleLineComment = 292;
	int MultiLineComment = 293;
	int Letter = 294;
	int Digit = 295;
	int Exponent = 296;
	int Number = 297;
}
