package models.constant;

public class Settings {

	/**
	 * Log directory. Will be created if not exists.
	 */
	public static final String LOG_DIRECTORY = "C:\\logs\\JFight\\";

	/**
	 * If false, logs will be written to log files only. If true, logs will be shown
	 * in a console too.
	 */
	public static final boolean WRITE_LOGS_TO_CONSOLE = true;

	/**
	 * Expire time in seconds.
	 * Item will be deleted from the cache if it will not be
	 * used longer than expire time. Max round time is 30 seconds, so item expire
	 * time should longer.
	 */
	public static final int CACHE_ITEM_EXPIRE_TIME = 31;

	/**
	 * Approximate expire time in minutes.
	 * Every period cache service deletes expired data itself.
	 * The time of a fight is somewhere about 3 minutes. So 5 minutes should be ok.
	 */
	public static final int CACHE_EXPIRED_DATA_CLEANUP_PERIOD = 5;
	/**
	 * 	Determines how much time wait for player action in seconds
	 */
	public static final long PLAYER_ACTION_WAITING_TIME = 35;
	/**
	 * One second in miliseconds
	 */
	public static final long ONE_SECOND = 1000;
}
