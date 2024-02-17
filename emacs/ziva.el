;;; ziva.el --- Minor mode to code Ziva in Emacs     -*- lexical-binding: t; -*-

;; Copyright (C) 2024  Roger Pibernat

;; Author: Roger Pibernat <alo@rogerpibernat.com>
;; Keywords:

;; This program is free software; you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <https://www.gnu.org/licenses/>.

;;; Commentary:

;;

;;; Code:

(setq ziva-tracks-map '((a "~t1 s: \\fmx >>>.1 1")
                        (s "~t2 s: \\fmx >>>.2 1")
                        (d "~t3 s: \\fmx >>>.3 1")
                        (f "~t4 s: \\fmx >>>.4 1")
                        (g "~t5 s: \\fmx >>>.5 1")
                        (h "~t6 s: \\fmx >>>.6 1")
                        (j "~t7 s: \\fmx >>>.7 1")
                        (k "~t8 s: \\fmx >>>.8 1")
                        ))

;; (defun ziva-new-sound (snd)
;;   "Create a new sound with a synth or a sample."
;;   (interactive)))

(defun ziva-get-command (cmd map)
  "Get CMD from ziva-cmd-map."
  (car (cdr (assoc (intern cmd) map))))

(defun ziva-insert (cmd)
  "Insert text CMD in buffer."
  (interactive "sCommand: ")
  (insert (ziva-get-command cmd ziva-tracks-map))
  )

(defun ziva-parse-cmds (cmds)
  "Convert CMDS to code."
  (let (chars result cmd-string)
    (dolist (cmd cmds)
      (setq cmd-string (ziva-get-command cmd))
      (setq result (concat result cmd-string)))
    result))

(defun ziva-read-cmds ()
  "Prompt for a command string."
  (let (cmds cmd done)
    (while (not done)
      ;; (setq cmd (read-string (format "cmd %s: " cmds)))
      (setq cmd (read-string (format "cmd %s: " (ziva-parse-cmds (reverse cmds)))))
      (if (string-empty-p cmd)
          (setq done t)
        (push cmd cmds)))
    (nreverse cmds)))

(defun ziva-livecoder ()
  "Livecoding assistant."
  (interactive)
  (insert (ziva-parse-cmds (ziva-read-cmds))))
  ;; (ziva-run-cmd (ziva-read-cmds)))

(map! :map sclang-mode-map :localleader :n "z" 'ziva-livecoder)
(map! :map sclang-mode-map :localleader :n "l" 'ziva-insert)


(provide 'ziva)
;;; ziva.el ends here
