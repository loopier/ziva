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

(define-minor-mode ziva-mode
  "Toggles local ziva-mode."
  nil   ; Initial value, disabled
  :global nil
  :group 'ziva
  :lighter "ziva"
  ;; :keymap

  (if ziva-mode
      (message "ziva-mode activated")
    (message "ziva-mode deactivated")))

(add-hook 'ziva-mode-hook (lambda () (message "Ziva hook executed")))
(add-hook 'ziva-mode-on-hook (lambda () (message "Ziva turned on")))
(add-hook 'ziva-mode-off-hook (lambda () (message "Ziva turned off")))

;; (map! :leader :n "o s" 'sclang-start :desc "Open Supercollider") ;; global under 'SPC-o' menu
;; (map! (:map ziva-mode-map :localleader :n "m" 'sclang-start))
;; (map! :map sclang-mode-map :localleader :n "z" 'ziva-livecoder)
(map! :map ziva-mode-map :localleader :n "a" 'ziva-agent)
(map! :map ziva-mode-map :localleader :n "i" 'ziva-insert)

(map! (:map sclang-mode-map :localleader :n "z b" 'ziva-boot))
(map! (:map sclang-mode-map :localleader :n "t i t" 'ziva-insert-track))
(map! (:map sclang-mode-map :localleader :n "t t" 'ziva-goto-track))
(map! (:map sclang-mode-map :localleader :n "t a" 'ziva-goto-amp))
(map! (:map sclang-mode-map :localleader :n "t f" 'ziva-goto-freq))
(map! (:map sclang-mode-map :localleader :n "t o" 'ziva-goto-octave))
(map! (:map sclang-mode-map :localleader :n "t l" 'ziva-goto-legato))
(map! (:map sclang-mode-map :localleader :n "t k" 'ziva-goto-atk))
(map! (:map sclang-mode-map :localleader :n "t d" 'ziva-goto-dec))
(map! (:map sclang-mode-map :localleader :n "t s" 'ziva-goto-sus))
(map! (:map sclang-mode-map :localleader :n "t r" 'ziva-goto-rel))
(map! (:map sclang-mode-map :localleader :n "l v" 'ziva-lfo-value))
(map! (:map sclang-mode-map :localleader :n "l s" 'ziva-lfo-sine))
(map! (:map sclang-mode-map :localleader :n "l t" 'ziva-lfo-tri))
(map! (:map sclang-mode-map :localleader :n "l z" 'ziva-lfo-saw))
(map! (:map sclang-mode-map :localleader :n "l p" 'ziva-lfo-pulse))
(map! (:map sclang-mode-map :localleader :n "l n r" 'ziva-lfo-noise0))
(map! (:map sclang-mode-map :localleader :n "l n l" 'ziva-lfo-noise1))
(map! (:map sclang-mode-map :localleader :n "l n e" 'ziva-lfo-noise2))
(map! (:map sclang-mode-map :localleader :n "l f" 'ziva-lfo-freq))
(map! (:map sclang-mode-map :localleader :n "l q" 'ziva-lfo-resonance))
(map! (:map sclang-mode-map :localleader :n "l l i" 'ziva-lfo-line-out))
(map! (:map sclang-mode-map :localleader :n "l l o" 'ziva-lfo-line-in))

(defun ziva-boot () (interactive) (insert "Ziva.boot;"))

(defun ziva-insert-track (name instrument mixer-track)
  "Insert a new track with a NAME and INSTRUMENT to the MIXER-TRACK number."
  (interactive "sName: \nsInstrument: \nnMixer Track: ")
  (insert (format "~%s s: \\%s >>>.%d 1;" name instrument mixer-track)))

(defun ziva-goto-track (name)
  (interactive "sTrack: ")
  (if (search-forward-regexp (format "^~%s" name) nil t)
      nil
    (search-backward-regexp (format "^~%s" name))))

(defun ziva-goto-track-param (track param)
  "Go to tha TRACK's PARAMeter value, or create it if is doesn't exist."
  (ziva-goto-track track)
  (if (search-forward-regexp (format "^~%s .* %s: " track param) nil t)
      nil
    (ziva-insert-track-param param)))

(defun ziva-insert-track-param (param)
  (ziva-goto-track-last-param)
  (insert (format " %s: " param))
  (goto-char (- (point) 1))
  (evil-append 1))

(defun ziva-goto-track-last-param ()
  (if (search-forward ">>>")
      (goto-char (- (point) 4))
    (search-forward ";")
    (goto-char (- (point 1)))))

(defun ziva-goto-amp (track) (interactive "sTrack: \n") (ziva-goto-track-param track "amp"))
(defun ziva-goto-freq (track) (interactive "sTrack: \n") (ziva-goto-track-param track "freq"))
(defun ziva-goto-octave (track) (interactive "sTrack: \n") (ziva-goto-track-param track "octave"))
(defun ziva-goto-legato (track) (interactive "sTrack: \n") (ziva-goto-track-param track "legato"))
(defun ziva-goto-atk (track) (interactive "sTrack: \n") (ziva-goto-track-param track "atk"))
(defun ziva-goto-dec (track) (interactive "sTrack: \n") (ziva-goto-track-param track "dec"))
(defun ziva-goto-sus (track) (interactive "sTrack: \n") (ziva-goto-track-param track "sus"))
(defun ziva-goto-rel (track) (interactive "sTrack: \n") (ziva-goto-track-param track "rel"))


(defun ziva-insert-lfo (name value)
  (insert (format "~%s lfo: %s;" name value)))

(defun ziva-lfo-func (name func freq)
  (ziva-insert-lfo name (format "%s(%s, 0, 1)" func freq)))

(defun ziva-lfo-sine (name freq) (interactive "sName: \nsFreq: \n") (ziva-lfo-func name "sine" freq))
(defun ziva-lfo-tri (name freq) (interactive "sName: \nsFreq: \n") (ziva-lfo-func name "tri" freq))
(defun ziva-lfo-saw (name freq) (interactive "sName: \nsFreq: \n") (ziva-lfo-func name "saw" freq))
(defun ziva-lfo-pulse (name freq) (interactive "sName: \nsFreq: \n") (ziva-lfo-func name "pulse" freq))
(defun ziva-lfo-noise0 (name freq) (interactive "sName: \nsFreq: \n") (ziva-lfo-func name "noise0" freq))
(defun ziva-lfo-noise1 (name freq) (interactive "sName: \nsFreq: \n") (ziva-lfo-func name "noise1" freq))
(defun ziva-lfo-noise2 (name freq) (interactive "sName: \nsFreq: \n") (ziva-lfo-func name "noise2" freq))


(defun ziva-agent (agent)
  "Choose an agent.
If an agent doesn't exist, it creates it.
Otherwise it goes to it."
  (interactive "sAgent: ")
  (ziva-find-agent agent)
  ;; (insert agent)
  )

(defun ziva-find-agent (agent)
  (copy-matching-lines agent))

;; (setq ziva-tracks-map '((a "~t1 s: \\fmx >>>.1 1")
;;                         (s "~t2 s: \\fmx >>>.2 1")
;;                         (d "~t3 s: \\fmx >>>.3 1")
;;                         (f "~t4 s: \\fmx >>>.4 1")
;;                         (g "~t5 s: \\fmx >>>.5 1")
;;                         (h "~t6 s: \\fmx >>>.6 1")
;;                         (j "~t7 s: \\fmx >>>.7 1")
;;                         (k "~t8 s: \\fmx >>>.8 1")
;;                         ))

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


(provide 'ziva)
;;; ziva.el ends here
