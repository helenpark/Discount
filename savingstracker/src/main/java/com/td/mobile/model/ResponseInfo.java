package com.td.mobile.model;

import java.io.Serializable;

public class ResponseInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ReasonCD;
	private ChallengeInfo Challenge;


	public static class ReasonCodes implements Serializable {
		private static final long serialVersionUID = 1L;

		public static final String MCHALL = "MCHALL";
		public static final String MLOCK = "MLOCK";
		public static final String MSETUP = "MSETUP";
		public static final String MTO="MTO";
	}


	public ResponseInfo(){
		super();
	}

	public String getReasonCD() {
		return ReasonCD;
	}
	public void setReasonCD(String reasonCD) {
		ReasonCD = reasonCD;
	}

	public ChallengeInfo getChallenge() {
		return Challenge;
	}

	public void setChallenge(ChallengeInfo challenge) {
		Challenge = challenge;
	}

	public class ChallengeInfo implements Serializable {
		private static final long serialVersionUID = 1L;

		private String Question;
		private String  QuestionID;
		public String getQuestion() {
			return Question;
		}
		public void setQuestion(String question) {
			Question = question;
		}
		public String getQuestionID() {
			return QuestionID;
		}
		public void setQuestionID(String questionID) {
			QuestionID = questionID;
		}


	}


}
