package xyz.ansidev.simple_blog.util;

import xyz.ansidev.simple_blog.enumeration.HtmlTagType;

public class HtmlUtils {

	public static final String BEGIN_OPEN_TAG = "<";
	public static final String END_OPEN_TAG = ">";
	public static final String BEGIN_CLOSE_TAG = "</";
	public static final String END_CLOSE_TAG = ">";

	/**
	 * Get HTML tag based on tag name and tag type (open tag or close tag).
	 * @param tagName Tag name
	 * @param htmlTagType
	 * @return HTML tag
	 */
	private static String getHtmlTag(String tagName, Enum<?> htmlTagType) {

		if (HtmlTagType.OPEN.equals(htmlTagType) || HtmlTagType.SINGLE.equals(htmlTagType)) {
			return BEGIN_OPEN_TAG + tagName + END_OPEN_TAG;
		} else if (HtmlTagType.CLOSE.equals(htmlTagType)) {
			return BEGIN_CLOSE_TAG + tagName + END_CLOSE_TAG;
		} else {
			return null;
		}
	}

	/**
	 * Get HTML open tag.
	 * @param tagName Tag name
	 * @return HTML open tag
	 */
	public static String getOpenHtmlTag(String tagName) {
		return getHtmlTag(tagName, HtmlTagType.OPEN);
	}

	/**
	 * Get HTML close tag.
	 * @param tagName Tag name
	 * @return HTML close Tag
	 */
	public static String getCloseHtmlTag(String tagName) {
		return getHtmlTag(tagName, HtmlTagType.CLOSE);
	}

	/**
	 * Get Single HTML tag.
	 * @param tagName Tag name
	 * @return Single HTML Tag
	 */
	public static String getSingleHtmlTag(String tagName) {
		return getHtmlTag(tagName, HtmlTagType.SINGLE);
	}

	/**
	 * Render HTML Code based on tag name and input content.
	 * @param tagName Tag name
	 * @param content Input Content
	 * @return Rendered HTML Code
	 */
	public static String renderHtmlCode(String tagName, String content) {
		return getOpenHtmlTag(tagName) + content + getCloseHtmlTag(tagName);
	}

}
