package org.mdevlamynck.qttt.common.network.messages;

public enum EGame {

	START,			/// (Server)	param : num player (1 or 2)

	REQUEST_TURN,	/// (Server)	reply : REPLY_TURN
	REPLY_TURN,		/// (Game)		param : Turn
	REQUEST_CHOICE,	/// (Server)	reply : REPLY_CHOICE
	REPLY_CHOICE,	/// (Game)		param : Choice

	OTHER_TURN,		/// (Server)	param : Turn
	OTHER_CHOICE,	/// (Server)	param : Choice

	FINISHED,		/// (Server)	param : GameResult
	INTERRUPTED,	/// (Server)

	NONE

}
