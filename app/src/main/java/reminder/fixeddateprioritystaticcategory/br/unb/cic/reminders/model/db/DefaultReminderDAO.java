package reminder.fixeddateprioritystaticcategory.br.unb.cic.reminders.model.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import
reminder.fixeddateprioritystaticcategory.br.unb.cic.framework.persistence.DBException;
import
reminder.fixeddateprioritystaticcategory.br.unb.cic.framework.persistence.DBInvalidEntityException;
import
reminder.fixeddateprioritystaticcategory.br.unb.cic.framework.persistence.GenericDAO;
import
reminder.fixeddateprioritystaticcategory.br.unb.cic.reminders.model.Reminder;
import
reminder.fixeddateprioritystaticcategory.br.unb.cic.reminders.model.Priority;
import
reminder.fixeddateprioritystaticcategory.br.unb.cic.reminders.model.Category;
/*** added by dManageReminder* modified by dPriority* modified by
dStaticCategory
 */
public class DefaultReminderDAO extends GenericDAO<Reminder> implements
ReminderDAO {
	public DefaultReminderDAO(Context c) {
		super(c);
	}
	public Long saveReminder(Reminder r) throws DBException {
		try {
			return persist(r);
		}
		catch(DBInvalidEntityException e) {
			throw new DBException();
		}
	}
	public void updateReminder(Reminder reminder) throws DBException {
		try {
			persist(reminder);
		}
		catch(DBInvalidEntityException e) {
			throw new DBException();
		}
		finally {
			db.close();
			dbHelper.close();
		}
	}
	public void persistReminder(Reminder reminder) throws DBException {
		try {
			persist(reminder);
		}
		catch(DBInvalidEntityException e) {
			throw new DBException();
		}
		finally {
			db.close();
			dbHelper.close();
		}
	}
	private Reminder cursorToReminder(Cursor cursor) throws DBException {
		Long pk =
		cursor.getLong(cursor.getColumnIndex(DBConstants.REMINDER_PK_COLUMN));
		String text =
		cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_TEXT_COLUMN));
		String details =
		cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_DETAILS_COLUMN));
		int done =
		cursor.getInt(cursor.getColumnIndex(DBConstants.REMINDER_DONE_COLUMN));
		Reminder reminder = createReminderCursor(cursor);
		reminder.setText(text);
		reminder.setDetails(details);
		reminder.setId(pk);
		reminder.setDone(done);
		return reminder;
	}
	/*** modified by dPriority* modified by dStaticCategory
	 */
	private Reminder createReminderCursor(Cursor cursor) throws DBException {
		Reminder reminder = createReminderCursor_original2(cursor);
		Long categoryId =
		cursor.getLong(cursor.getColumnIndex(DBConstants.REMINDER_FK_CATEGORY_COLUMN));
		Category category =
		DBFactory.factory(context).createCategoryDAO().findCategoryById(categoryId);
		reminder.setCategory(category);
		return reminder;
	}
	private List<Reminder> remindersFromCursor(Cursor cursor) throws DBException {
		List<Reminder> reminders = new ArrayList<Reminder>();
		if(cursor.moveToFirst()) {
			do {
				Reminder reminder = cursorToReminder(cursor);
				reminders.add(reminder);
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		return reminders;
	}
	/*** modified by dPriority
	 */
	private Reminder createReminderCursor_original0(Cursor cursor) throws
	DBException {
		Reminder reminder = new Reminder();
		String date =
		cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_DATE_COLUMN));
		String hour =
		cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_HOUR_COLUMN));
		reminder.setDate(date);
		reminder.setHour(hour);
		return reminder;
	}
	/*** added by dStaticCategory
	 */
	public List<Reminder> listRemindersByCategory(Category category) throws
	DBException {
		try {
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(DBConstants.SELECT_REMINDERS_BY_CATEGORY, new
				String [] {
					category.getId().toString()
				});
			return remindersFromCursor(cursor);
		}
		catch(Exception e) {
			Log.e(DefaultCategoryDAO.class.getCanonicalName(), e.getLocalizedMessage());
			throw new DBException();
		}
		finally {
			db.close();
			dbHelper.close();
		}
	}
	/*** added by dStaticCategory
	 */
	public List<Reminder> listReminders() throws DBException {
		try {
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(DBConstants.SELECT_REMINDERS, null);
			return remindersFromCursor(cursor);
		}
		catch(Exception e) {
			Log.e(DefaultCategoryDAO.class.getCanonicalName(), e.getLocalizedMessage());
			throw new DBException();
		}
		finally {
			db.close();
			dbHelper.close();
		}
	}
	/*** added by dStaticCategory
	 */
	public void deleteReminder(Reminder reminder) throws DBException {
		try {
			db = dbHelper.getWritableDatabase();
			db.delete(DBConstants.REMINDER_TABLE, DBConstants.REMINDER_PK_COLUMN + "=" +
				reminder.getId(), null);
		}
		catch(SQLiteException e) {
			Log.e(DefaultCategoryDAO.class.getCanonicalName(), e.getLocalizedMessage());
			throw new DBException();
		}
		finally {
			db.close();
			dbHelper.close();
		}
	}
	/*** modified by dPriority* modified by dStaticCategory
	 */
	private Reminder createReminderCursor_original2(Cursor cursor) throws
	DBException {
		Reminder reminder = createReminderCursor_original0(cursor);
		int priority =
		cursor.getInt(cursor.getColumnIndex(DBConstants.REMINDER_PRIORITY_COLUMN));
		reminder.setPriority(Priority.fromCode(priority));
		return reminder;
	}
}