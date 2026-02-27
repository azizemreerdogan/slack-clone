-- =============================================
-- V1: Create all tables for Slack Clone
-- =============================================

-- ENUM types
CREATE TYPE user_status_enum AS ENUM ('ONLINE', 'OFFLINE', 'AWAY', 'DO_NOT_DISTURB');
CREATE TYPE workspace_status_enum AS ENUM ('ACTIVE', 'INACTIVE', 'ARCHIVED');
CREATE TYPE member_status_enum AS ENUM ('ACTIVE', 'INACTIVE', 'BANNED', 'INVITED');
CREATE TYPE role_type_enum AS ENUM ('OWNER', 'ADMIN', 'MEMBER', 'GUEST');
CREATE TYPE channel_type_enum AS ENUM ('PUBLIC', 'PRIVATE', 'DIRECT');
CREATE TYPE notification_type_enum AS ENUM ('MENTION', 'REACTION', 'THREAD_REPLY', 'DIRECT_MESSAGE', 'CHANNEL_INVITE');
CREATE TYPE entity_type_enum AS ENUM ('MESSAGE', 'CHANNEL', 'WORKSPACE');

CREATE TABLE users (
    usr_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    user_status user_status_enum NOT NULL DEFAULT 'OFFLINE'
);

CREATE TABLE workspace (
    w_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    slug VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    status workspace_status_enum NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE workspace_members (
    mem_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_usr_id UUID NOT NULL REFERENCES users(usr_id) ON DELETE CASCADE,
    workspace_w_id UUID NOT NULL REFERENCES workspace(w_id) ON DELETE CASCADE,
    role role_type_enum NOT NULL DEFAULT 'MEMBER',
    display_name VARCHAR(100),
    member_status member_status_enum NOT NULL DEFAULT 'ACTIVE',
    joined_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (user_usr_id, workspace_w_id)
);

CREATE TABLE channel (
    channel_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    channel_type channel_type_enum NOT NULL DEFAULT 'PUBLIC',
    is_archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE channel_member (
    chmem_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    channel_channel_id UUID NOT NULL REFERENCES channel(channel_id) ON DELETE CASCADE,
    workspace_members_mem_id UUID NOT NULL REFERENCES workspace_members(mem_id) ON DELETE CASCADE,
    UNIQUE (channel_channel_id, workspace_members_mem_id)
);

CREATE TABLE messages (
    msg_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    channel_id UUID NOT NULL REFERENCES channel(channel_id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES workspace_members(mem_id) ON DELETE CASCADE,
    content TEXT,
    parent_msg_id UUID REFERENCES messages(msg_id) ON DELETE SET NULL,
    is_edited BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    edited_at TIMESTAMP
);

CREATE TABLE notifications (
    notif_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workspace_w_id UUID NOT NULL REFERENCES workspace_members(mem_id) ON DELETE CASCADE,
    entity_type entity_type_enum NOT NULL,
    entity_id UUID NOT NULL,
    notification_type notification_type_enum NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_read BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_messages_channel_id ON messages(channel_id);
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_parent_msg_id ON messages(parent_msg_id);
CREATE INDEX idx_workspace_members_user ON workspace_members(user_usr_id);
CREATE INDEX idx_workspace_members_workspace ON workspace_members(workspace_w_id);
CREATE INDEX idx_notifications_workspace_member ON notifications(workspace_w_id);
CREATE INDEX idx_channel_member_channel ON channel_member(channel_channel_id);
CREATE INDEX idx_channel_member_member ON channel_member(workspace_members_mem_id);