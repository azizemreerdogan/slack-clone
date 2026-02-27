-- =============================================
-- V2: Triggers for automatic timestamp updates
--     and business logic
-- =============================================

-- -----------------------------------------------
-- FUNCTION: auto-update updated_at on users
-- -----------------------------------------------
CREATE OR REPLACE FUNCTION trigger_set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: users.updated_at
CREATE TRIGGER set_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_updated_at();

-- Trigger: workspace.updated_at
CREATE TRIGGER set_workspace_updated_at
    BEFORE UPDATE ON workspace
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_updated_at();

-- -----------------------------------------------
-- FUNCTION: auto-set messages.edited_at on update
-- -----------------------------------------------
CREATE OR REPLACE FUNCTION trigger_set_message_edited_at()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.content IS DISTINCT FROM OLD.content THEN
        NEW.is_edited = TRUE;
        NEW.edited_at = NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: messages edited_at + is_edited
CREATE TRIGGER set_message_edited_at
    BEFORE UPDATE ON messages
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_message_edited_at();

-- -----------------------------------------------
-- FUNCTION: prevent thread depth > 1
--   (replies to replies are not allowed)
-- -----------------------------------------------
CREATE OR REPLACE FUNCTION trigger_check_thread_depth()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.parent_msg_id IS NOT NULL THEN
        -- If the parent already has a parent, it is a 2nd-level thread — reject
        IF EXISTS (
            SELECT 1 FROM messages
            WHERE msg_id = NEW.parent_msg_id
              AND parent_msg_id IS NOT NULL
        ) THEN
            RAISE EXCEPTION 'Thread depth cannot exceed 1. Replies to replies are not allowed.';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: enforce max thread depth of 1
CREATE TRIGGER check_thread_depth
    BEFORE INSERT ON messages
    FOR EACH ROW
    EXECUTE FUNCTION trigger_check_thread_depth();

-- -----------------------------------------------
-- FUNCTION: soft-delete messages (mark as deleted)
--   instead of physically removing them so threads
--   are preserved
-- -----------------------------------------------
CREATE OR REPLACE FUNCTION trigger_soft_delete_parent_message()
RETURNS TRIGGER AS $$
BEGIN
    -- When a parent message is "deleted", mark children too
    IF NEW.is_deleted = TRUE AND OLD.is_deleted = FALSE THEN
        UPDATE messages
        SET is_deleted = TRUE
        WHERE parent_msg_id = NEW.msg_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: cascade soft-delete to thread replies
CREATE TRIGGER soft_delete_parent_message
    AFTER UPDATE OF is_deleted ON messages
    FOR EACH ROW
    EXECUTE FUNCTION trigger_soft_delete_parent_message();

